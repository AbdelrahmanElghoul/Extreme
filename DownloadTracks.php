<?php
$con=new mysqli("localhost","id8537262_elghoul","Elghoul1832","id8537262_extreme");	

$sql="select trackName from Tracks";
$result = $con->query($sql);
$data=array();

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $data[]=$row["trackName"];
    }
	$response["success"]=true;
}else{
	$response["success"]=false;
}
$con->close();	
echo json_encode(array('data' => $data, 'response' => $response));
?>


