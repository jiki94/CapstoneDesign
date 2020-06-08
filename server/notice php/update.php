<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST'){
    
    $id = $_POST['id'];
    $title = $_POST['title'];
    $note = $_POST['note'];
    $color = $_POST['color'];
    
    require_once("connect.php");
    
    $query = "UPDATE `notes` SET title='$title', note='$note', color='$color' WHERE id='$id'";
    
    if(mysqli_query($conn, $query)){
        
        $response['success'] = true;
        $response['message'] = "Successfully";
    }else{
        
        $response['success'] = false;
        $response['message'] = "Failure!";
    }
}else{
    
    $response['success'] = false;
    $response['message'] = "Error!";
}

echo json_encode($response);

?>
