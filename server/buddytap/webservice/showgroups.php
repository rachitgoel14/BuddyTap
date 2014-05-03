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

$query = "Select * FROM groupid WHERE username=:user";

$query_params = array(
        ':user' => $_POST['username']
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
    $response["message"] = "Groups Available!";
    $response["posts"]   = array();
    
    foreach ($rows as $row) {
        $post             = array();
		$post["gid"]  = $row["gid"];
        $post["groupname"] = $row["groupname"];
        $post["username"] = $row["username"];
        
        
        //update our repsonse JSON data
        array_push($response["posts"], $post);
    }
    
    // echoing JSON response
    echo json_encode($response);
    
    
} else {
    $response["success"] = 0;
    $response["message"] = "No Groups Available!";
    die(json_encode($response));
}
}else{
?>
		<h1>Login</h1> 
		<form action="showgroups.php" method="post"> 
		    
		    Username:<br /> 
		    <input type="text" name="username" placeholder="username" value="" /> 
		    <br /><br /> 
		    <input type="submit" value="Find Groups for this user" /> 
		</form> 
		
	<?php
}
?>
