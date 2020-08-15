package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class RecipeInfoRepository {
    private final AppRoomDatabase database;
    private final RecipeInfoDao recipeInfoDao;
    private final MainIngredientCountDao mainIngredientCountDao;
    private final DislikedRecipeDao dislikedRecipeDao;
    private final RecipeIngredientDao recipeIngredientDao;
    private final ProductDao productDao;

    LiveData<List<Integer>> dislikedLiveData;
    LiveData<List<RecipeInfo>> recipesLiveData;
    LiveData<List<MainIngredientCount>> mainIngredientsCountLiveData;
    LiveData<List<RecipeIngredient>> recipeIngredientLiveData;
    LiveData<List<String>> notOverdueProductLiveData;
    LiveData<List<String>> almostOverdueProductLiveData;
    MediatorLiveData<List<RecipeInfoAndAlmost>> result;

    LiveData<List<RecipeInfo>> searchedRecipesLiveData;
    MediatorLiveData<List<RecipeInfoAndAlmost>> searchedResult;

    // merge 할 때 필요한 정보
    // rp = RecipeInfo
    // mic = MainIngredientCount
    // ri = RecipeIngredient
    // nop = notOverdueProduct
    // aop = almostOverdueProduct
    private List<RecipeInfo> rp;
    private Map<Integer, Integer> mic;
    private List<RecipeIngredient> ri;
    private List<String> nop;
    private List<String> aop;

    private List<RecipeInfo> srp;

    public RecipeInfoRepository(AppRoomDatabase database) {
        this.database = database;
        this.recipeInfoDao = database.recipeInfoDao();
        this.mainIngredientCountDao = database.mainIngredientCountDao();
        this.dislikedRecipeDao = database.dislikedRecipeDao();
        this.recipeIngredientDao = database.recipeIngredientDao();
        this.productDao = database.productDao();

        setMerger();

        dislikedLiveData = dislikedRecipeDao.getDisliked();
        recipesLiveData = Transformations.switchMap(dislikedLiveData, recipeInfoDao::getRecipesExclude);
        mainIngredientsCountLiveData = Transformations.switchMap(dislikedLiveData, mainIngredientCountDao::getCountsExclude);
        recipeIngredientLiveData = Transformations.switchMap(dislikedLiveData, recipeIngredientDao::getIngredientsExclude);
        notOverdueProductLiveData = null;
        almostOverdueProductLiveData = null;

        result = new MediatorLiveData<>();
        result.addSource(recipesLiveData, recipes -> {
            rp = recipes;
            merge(result);
        });

        result.addSource(mainIngredientsCountLiveData, mainIngredientCounts -> {
            mic = new TreeMap<>();
            for (MainIngredientCount ingredientCount : mainIngredientCounts) {
                mic.put(ingredientCount.recipeCode, ingredientCount.count);
            }

            merge(result);
        });

        result.addSource(recipeIngredientLiveData, recipeIngredients -> {
            ri = recipeIngredients;
            merge(result);
        });

        searchedResult = new MediatorLiveData<>();
        searchedResult.addSource(recipeIngredientLiveData, recipeIngredients -> {
            ri = recipeIngredients;
            searchMerge(searchedResult);
        });
    }

    public LiveData<List<RecipeInfoAndAlmost>> getRecommendRecipes(){
        if (notOverdueProductLiveData != null) {
            result.removeSource(notOverdueProductLiveData);
            notOverdueProductLiveData = null;
        }

        if (almostOverdueProductLiveData != null) {
            result.removeSource(almostOverdueProductLiveData);
            almostOverdueProductLiveData = null;
        }

        notOverdueProductLiveData = productDao.getItemsNotOverdue(LocalDate.now());
        almostOverdueProductLiveData = productDao.getItemsBetween(LocalDate.now(), LocalDate.now().plusDays(3));

        result.addSource(notOverdueProductLiveData, notOverdueProduct -> {
            nop = notOverdueProduct;
            merge(result);
        });

        result.addSource(almostOverdueProductLiveData, almostOverdueProduct -> {
            aop = almostOverdueProduct;
            Collections.sort(aop);
            merge(result);
        });

        return result;
    }

    private void setMerger() {
        rp = null;
        mic = null;
        ri = null;
        nop = null;
        aop = null;

        srp = null;
    }

    private void merge(MutableLiveData<List<RecipeInfoAndAlmost>> retLiveData) {
        ArrayList<RecipeInfoAndAlmost> ret = new ArrayList<RecipeInfoAndAlmost>();
        if (rp == null || mic == null || ri == null || nop == null || aop == null){
            retLiveData.setValue(ret);
            return;
        }

        // 레시피에서 유통기한 임박이랑 그냥 메인 재료 개수를 센다.
        TreeMap<Integer, Integer> almostCount = new TreeMap<>();
//        TreeMap<Integer, Integer> mainCount = new TreeMap<>();
        for (RecipeIngredient ingredient : ri) {

            if (ingredient.ingredientType == 3060001) {
//                int curValue = customGetOrDefault(mainCount, ingredient.recipeCode, 0);
//                mainCount.put(ingredient.recipeCode, curValue + 1);

                if (Collections.binarySearch(aop, ingredient.ingredientName) >= 0) {
                    int curValue = customGetOrDefault(almostCount, ingredient.recipeCode, 0);
                    almostCount.put(ingredient.recipeCode, curValue + 1);
                }
            }
        }

        Collections.sort(rp, (lhs, rhs) -> {
            int lhsValue = customGetOrDefault(almostCount, lhs.recipeCode, 0);
            int rhsValue = customGetOrDefault(almostCount, rhs.recipeCode, 0);

            return -(lhsValue -  rhsValue);
        });


        for (RecipeInfo recipeInfo : rp) {
            ret.add(new RecipeInfoAndAlmost(recipeInfo, almostCount.containsKey(recipeInfo.recipeCode)));
        }

        retLiveData.setValue(ret);
    }

    // map getOrDefault not supported in current java
    private <K, V> V customGetOrDefault(Map<K, V> mp, K key, V defaultValue) {
        V curStored = mp.get(key);
        if (curStored == null)
            curStored = defaultValue;

        return curStored;
    }

    public LiveData<List<RecipeInfo>> getRecommendRecipes_before(){
        return recipeInfoDao.getRecommendRecipes(LocalDate.now());
    }

    public LiveData<RecipeInfo> getRecipeInfo(int recipeCode){
        return recipeInfoDao.getRecipeInfo(recipeCode);
    }

    public LiveData<List<RecipeInfoAndAlmost>> getRecipeInfoSearchBy(String searchWord){
        if (searchedRecipesLiveData != null) {
            searchedResult.removeSource(searchedRecipesLiveData);
            searchedRecipesLiveData = null;
        }

        if (almostOverdueProductLiveData != null) {
            result.removeSource(almostOverdueProductLiveData);
            almostOverdueProductLiveData = null;
        }

        searchedRecipesLiveData = recipeInfoDao.getRecommendRecipes(searchWord);
        searchedResult.addSource(searchedRecipesLiveData, searchedRecipes -> {
            srp = searchedRecipes;
            searchMerge(searchedResult);
        });

        almostOverdueProductLiveData = productDao.getItemsBetween(LocalDate.now().minusDays(3), LocalDate.now());

        result.addSource(almostOverdueProductLiveData, almostOverdueProduct -> {
            aop = almostOverdueProduct;
            Collections.sort(aop);
            merge(result);
        });

        return searchedResult;
    }

    private void searchMerge(MutableLiveData<List<RecipeInfoAndAlmost>> retLiveData) {
        List<RecipeInfoAndAlmost> ret = new ArrayList<>();

        if (srp == null || ri == null || aop == null) {
            retLiveData.setValue(ret);
            return;
        }

        TreeMap<Integer, Integer> almostCount = new TreeMap<>();
        for (RecipeIngredient ingredient : ri) {

            if (ingredient.ingredientType == 3060001) {
                if (Collections.binarySearch(aop, ingredient.ingredientName) >= 0) {
                    int curValue = customGetOrDefault(almostCount, ingredient.recipeCode, 0);
                    almostCount.put(ingredient.recipeCode, curValue + 1);
                }
            }
        }

        Collections.sort(srp, (lhs, rhs) -> {
            int lhsValue = customGetOrDefault(almostCount, lhs.recipeCode, 0);
            int rhsValue = customGetOrDefault(almostCount, rhs.recipeCode, 0);

            return -(lhsValue -  rhsValue);
        });

        for (RecipeInfo recipeInfo : srp) {
            ret.add(new RecipeInfoAndAlmost(recipeInfo, almostCount.containsKey(recipeInfo.recipeCode)));
        }

        retLiveData.setValue(ret);
    }

    public LiveData<List<RecipeInfo>> getDislikedRecipe(){
        return recipeInfoDao.getDislikedRecipes();
    }
}
