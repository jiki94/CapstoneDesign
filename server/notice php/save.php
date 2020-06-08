<?php

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    
    $title = $_POST['title'];
    $note = $_POST['note'];
    $color = $_POST['color'];
    
    require_once("connect.php");
    
    $query = "INSERT INTO `notes` (title, note, color) VALUES ('$title', '$note', '$color')";
    
    if(mysqli_query($conn, $query)){
        
        $response['success'] = true;
        $response['message'] = "Successfully";
    } else{
        
        $response['success'] = false;
        $response['message'] = "Failure!";
    }
} else{
    
    $response['success'] = 'false';
    $response['message'] = "Error!";
}

echo json_encode($response);

?>