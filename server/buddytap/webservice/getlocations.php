<?php
error_reporting(0);
/*
Our "config.inc.php" file connects to database every time we include or require
it within a php script.  Since we want this script to add a new user to our db,
we will be talking with our database, and therefore,
let's require the connection to happen:
*/
require("config.inc.php");
if(!empty($_POST)){
//initial query
//$query = "Select * FROM locations";
$query = "Select * FROM user_locations INNER JOIN groupid ON user_locations.username=groupid.username WHERE gid=:gid";

$query_params = array(
        ':gid' => $_POST['gid']
    );
//execute query
try {
    $stmt   = $db->prepare($query);
    $result = $stmt->execute($query_params);
}
catch (PDOException $ex) {
    $response["success"] = 0;
    $response["message"] = "Database Error!";
    die(json_encode($response));
}

// Finally, we can retrieve all of the found rows into an array using fetchAll 
$rows = $stmt->fetchAll();


if ($rows) {
    $response["success"] = 1;
    $response["message"] = "Locations Available!";
    $response["posts"]   = array();
    
    foreach ($rows as $row) {
        $post             = array();
		$post["app_id"]  = $row["app_id"];
        $post["username"] = $row["username"];
        $post["latitude"]    = $row["latitude"];
        $post["longitude"]  = $row["longitude"];
         $post["address"]  = $row["address"];
        
        //update our repsonse JSON data
        array_push($response["posts"], $post);
    }
    
    // echoing JSON response
    echo json_encode($response);
    
    
} else {
    $response["success"] = 0;
    $response["message"] = "No Location Available!";
    die(json_encode($response));
}
}else{
?>
		<h1>Login</h1> 
		<form action="getlocations.php" method="post"> 
		    
		    GroupId:<br /> 
		    <input type="text" name="gid" placeholder="gid" value="" /> 
		    <br /><br /> 
		    <input type="submit" value="Find Users" /> 
		</form> 
		
	<?php
}
?>
