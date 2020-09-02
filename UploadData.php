<?php
$con=mysqli_connect("localhost","id8537262_elghoul","Elghoul1832","id8537262_extreme");

$name=$_POST["name"];
$phone=$_POST["phone"];
$email=	$_POST["email"];
$Year=$_POST["Year"];
$track=$_POST["track"];
$interViewDate=$_POST["interViewDate"];
$interViewTime=$_POST["interViewTime"];

$statement=mysqli_prepare($con,"INSERT INTO Applied (name,phone,email,Year,track,interViewDate,interViewTime) VALUES (?,?,?,?,?,?,?)");
mysqli_stmt_bind_param($statement,"sssssss",$name,$phone,$email,$Year,$track,$interViewDate,$interViewTime);
$response=array();

try{
mysqli_stmt_execute($statement);
$response["success"]=true;	
}catch(Exception $e){
$response["success"]=$e->getMessage();		
}
	


echo json_encode($response);
?>	