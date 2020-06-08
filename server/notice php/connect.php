<?php
$conn = mysqli_connect("localhost", "expapp", "tksrleo12!", "expapp");

//testing connection
if($conn){
echo "success";
} else{
echo "error";
}

?>