<?php
 require_once('dbconnect.php');
 if($_SERVER['REQUEST_METHOD']=='POST'){
 $username = $_POST['username'];
 $email = $_POST['email'];
 $password = $_POST['password'];
 

$sql1= "CREATE TABLE Class( ".
 "Class_id INT NOT NULL AUTO_INCREMENT, ".
 "Class_title VARCHAR(100) NOT NULL, ".
 "Class_author VARCHAR(40) NOT NULL, ".
 "Class_date DATE, ".
 "PRIMARY KEY ( Class_id )); ";
 
 //define your existing database name here.
mysql_select_db( 'u265362469_users' );
$retval = mysql_query( $sql1, $conn );
if(! $retval )
{
 die('Could not create table: ' . mysql_error());
}
echo "Table created successfully\n";
$sql = "INSERT INTO Registeration(username,password,email) VALUES ('$username','$email','$password')";
 
 
 if(mysqli_query($con,$sql)){
 echo "Successfully Registered";
 }else{
 echo "Could not register";
 
 }
 }else{
echo 'error';
}
 