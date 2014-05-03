<?php

//load and connect to MySQL database stuff
require("config.inc.php");

if (!empty($_POST)) {
	//initial query
	$query = "INSERT INTO user_locations ( username, latitude, longitude, address ) VALUES ( :user, :lat, :lon, :add ) ";

    //Update query
    $query_params = array(
        ':user' => $_POST['username'],
        ':lat' => $_POST['latitude'],
		':lon' => $_POST['longitude'],
		':add' => $_POST['address']
    );
  
	//execute query
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        // For testing, you could use a die and message. 
        //die("Failed to run query: " . $ex->getMessage());
        
        //or just use this use this one:
        $response["success"] = 0;
        $response["message"] = "Database Error. Couldn't add location!";
        die(json_encode($response));
    }

    $response["success"] = 1;
    $response["message"] = "Location Successfully Added!";
    echo json_encode($response);
   
} else {
?>
		<h1>Add Locations</h1> 
		<form action="savelocations.php" method="post"> 
		    Username:<br /> 
		    <input type="text" name="username" placeholder="username" /> 
		    <br /><br /> 
		    Latitude:<br /> 
		    <input type="text" name="latitude" placeholder="post latitude" /> 
		    <br /><br />
			Longitude:<br /> 
		    <input type="text" name="longitude" placeholder="post longitude" /> 
		    <br /><br />
			Address:<br /> 
		    <input type="text" name="address" placeholder="post address" /> 
		    <br /><br />
		    <input type="submit" value="Add Location" /> 
		</form> 
	<?php
}

?> 
