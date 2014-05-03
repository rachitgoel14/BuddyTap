<?php

//load and connect to MySQL database stuff
require("config.inc.php");

if (!empty($_POST)) {


 $query        = " SELECT 1 FROM groupid WHERE username = :user";
    //now lets update what :user should be
    $query_params = array(
        ':user' => $_POST['username']
    );
    
    //Now let's make run the query:
    try {
        // These two statements run the query against your database table. 
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        // For testing, you could use a die and message. 
        //die("Failed to run query: " . $ex->getMessage());
        
        //or just use this use this one to product JSON data:
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }
    
    //fetch is an array of returned data.  If any data is returned,
    //we know that the username is already in use, so we murder our
    //page
    $row = $stmt->fetch();
    if ($row) {
        // For testing, you could use a die and message. 
        //die("This username is already in use");
        
        //You could comment out the above die and use this one:
        $response["success"] = 0;
        $response["message"] = "You have already logged in";
        die(json_encode($response));
    }

    //gets user's info based off of a username.
    $query = " 
            SELECT 
                gid,groupname
            FROM groupid 
            WHERE 
                gid = :gid 
        ";
    
    $query_params = array(
        ':gid' => $_POST['gid']
    );
    
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        // For testing, you could use a die and message. 
        //die("Failed to run query: " . $ex->getMessage());
        
        //or just use this use this one to product JSON data:
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
        
    }
    
    //This will be the variable to determine whether or not the user's information is correct.
    //we initialize it as false.
    $validated_info = false;
    
    //fetching all the rows from the query
    $row = $stmt->fetch();
    if ($row) {
           if ($_POST['groupname'] === $row['groupname']) {
            $login_ok = true;
        }
		 $query = "INSERT INTO groupid ( gid, groupname,username ) VALUES ( :gid, :gname, :user) ";
    
    //Again, we need to update our tokens with the actual data:
    $query_params = array(
	    ':gid' => $_POST['gid'],
		':gname' => $_POST['groupname'],
        ':user' => $_POST['username']
        
    );
    
    //time to run our query, and create the user
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        // For testing, you could use a die and message. 
        //die("Failed to run query: " . $ex->getMessage());
        
        //or just use this use this one:
        $response["success"] = 0;
        $response["message"] = "Database Error2. Please Try Again!";
        die(json_encode($response));
    }	
        
    }
    
    // If the user logged in successfully, then we send them to the private members-only page 
    // Otherwise, we display a login failed message and show the login form again 
    if ($login_ok) {
        $response["success"] = 1;
        $response["message"] = "Login successful!";
        die(json_encode($response));
    } else {
        $response["success"] = 0;
        $response["message"] = "Group Id or Group Name is incorrect";
        die(json_encode($response));
    }
} else {
?>
		<h1>Login</h1> 
		<form action="login-gid.php" method="post"> 
		    Username:<br /> 
		    <input type="text" name="username" placeholder="username" /> 
		    <br /><br /> 
			 Groupname:<br /> 
		    <input type="text" name="groupname" placeholder="groupname" /> 
		    <br /><br /> 
		    GroupId:<br /> 
		    <input type="text" name="gid" placeholder="gid" value="" /> 
		    <br /><br /> 
		    <input type="submit" value="Login" /> 
		</form> 
		//<a href="register.php">Register</a>
	<?php
}

?> 