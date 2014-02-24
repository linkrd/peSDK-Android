SCAi PrizeSDK Android
=================

About the SDK
-------------

SCAi PrizeSDK Android can facilitate adding sweepstakes or instant win functionality to a new or existing Android app. By integrating the SDK in to your user system, you can give your users the opportunity to win prizes through SCAi's game engine. SCAi's game engine can manage the business rules, such as plays per day, per user and maximum plays for a promotion.

Configuration
-------------

Your sales representative will provide you with the following values:

  * client
  * promo
  * authkey

Set these values in your configuration settings variable, and initialize the class object with the settings. In the case of a configuration error, an exception is thrown.


Demo Configurations
-------------------

### Sweeps:
```java

    Map<String,String> configMap = new HashMap<String, String>();
    configMap.put("contest_adminID", unique_ID);
    configMap.put("client", "demosdk");
    configMap.put("promo", "sweeps");
    configMap.put("authkey", "DEMO-SDK1-1234-5678");
    
    try {  	
    	prizeSDK = new PeSDK(this, configMap, getApplicationContext());
    } catch (PeException e) {
    
    }

```                                            
### Instant Win (one level):
```java
    Map<String,String> configMap = new HashMap<String, String>();
    configMap.put("contest_adminID", unique_ID);
    configMap.put("client", "demosdk");
    configMap.put("promo", "instant");
    configMap.put("authkey", "DEMO-SDK1-1234-5678");
    
    try {  	
    	prizeSDK = new PeSDK(this, configMap, getApplicationContext());
    } catch (PeException e) {
    
    }
```                        
### Instant Win (multi level):
```java
	Map<String,String> configMap = new HashMap<String, String>();
    configMap.put("contest_adminID", unique_ID);
    configMap.put("client", "demosdk");
    configMap.put("promo", "instantmulti");
    configMap.put("authkey", "DEMO-SDK1-1234-5678");
    
    try {  	
    	prizeSDK = new PeSDK(this, configMap, getApplicationContext());
    } catch (PeException e) {
    
    }
    
```                              
                                              
Sample Code
-----------

A sample Eclipse project is provided. At a minimum, the SDK requires a username. Depending on your own user system, the business rules of the promotion and the rules required by the prizing contest, you may be required to provide more than a username. The SDK is meant to integrate with a user on your own system. This value must maintain consistency throughout the promotions. If you do not use a login or screen name or your login name does not comply with our system rules (under 25 chars and alphanumeric: 'a-z' '0-9' '-' and '_') or you allow users to change their screen name, you may use your userID as a username or the database index of the user's record. If your promotion does not require business rules regarding plays per user, you may use microtime or another sufficiently unique value for each play.
Please create your own test users to use with the examples provided. 

For the demos, one username, 'demo_infinite_plays' is permitted infinite plays. If you wish to test using this username, be aware, it does not behave like a regular user, and will not be restricted by the rules regarding plays per day. You should test with the user scheme you intend to use in your application.

The user data is shared between any promotions at a 'client' level, so any users you create and set the profile of, in demosdk-instant will have the same profile data in demosdk-sweeps. When you are ready for your promotion(s) to be set up, we will give you a new 'client' ID and none of the test users will exist in that configuration.

###User fields, types and lengths:

     
| Name          | Type          | Length | Default  |
| ------------- |:-------------:|:------:| :------: |
| username      | varchar       | 25     | NOT NULL |
| firstname     | varchar       | 25     | NULL     |
| lastname      | varchar       | 25     | NULL     |
| address       | varchar       | 50     | NULL     |
| address2      | varchar       | 50     | NULL     |
| city          | varchar       | 30     | NULL     |
| state         | varchar       | 25     | NULL     |
| country       | varchar       | 30     | NULL     |
| zip           | varchar       | 12     | NULL     |
| birthdate     | smalldatetime | 25     | NULL     |
| gender        | varchar       | 1      | NULL     |
| title         | varchar       | 100    | NULL     |
| company       | varchar       | 80     | NULL     |
| day_phone     | varchar       | 20     | NULL     |
| evening_phone | varchar       | 20     | NULL     |
| fax_phone     | varchar       | 20     | NULL     |
| mobile_email  | varchar       | 75     | NULL     |
| alt_email     | varchar       | 75     | NULL     |
| answer1       | varchar       | 50     | NULL     |
| answer2       | varchar       | 50     | NULL     |
| answer3       | varchar       | 50     | NULL     |
| answer4       | varchar       | 50     | NULL     |
| answer5       | varchar       | 50     | NULL     |
| answer6       | varchar       | 50     | NULL     |
| answer7       | varchar       | 50     | NULL     |
| answer8       | varchar       | 50     | NULL     |
| answer9       | varchar       | 50     | NULL     |
| answer10      | varchar       | 50     | NULL     |





PrizeSDK Functions
------------------

###Class Constructor

####new PrizeSDK 

Creates a new SDK object. 
```java

	String unique_ID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
	
    Map<String,String> configMap = new HashMap<String, String>();
    configMap.put("contest_adminID", unique_ID);
    configMap.put("client", "demosdk");
    configMap.put("promo", "instant");
    configMap.put("authkey", "DEMO-SDK1-1234-5678");
    
    try {  	
    	prizeSDK = new PeSDK(this, configMap, getApplicationContext());
    } catch (PeException e) {
    
    }
``` 
  
> params: an array containing the configuration values
>
> returns: [object] a PrizeSDK class object
>
> errors: throws an exception


###Configuration Data

* #### isOpen 
  Checks whether or not the contest is open.
  
  usage: `prizeSDK.isOpen()`

  > returns:[`Boolean`] `true` if contest is currently open. `false` for closed.


* #### getRequiredFields
  Get the required fields from the configuration values .

  usage: `prizeSDK.getRequiredFields();`
    
  > returns: [`JSONArray`] array of required fields


* #### getAllFields
  Get all possible profile fields from the configuration values.

  usage: `prizeSDK.getAllFields();`
    
  > returns: [`JSONArray`] array of all fields available

* #### getEntryPeriod
  Get all possible profile fields from the configuration values.

  usage: `prizeSDK.getEntryPeriod();`
    
  > returns: [`JSONObject`]  object of entry period information

* #### getPrizingInfo
  Gets the number of prize levels and the minimum and maximum level for instant win contest

  usage: `prizeSDK.getPrizingInfo();`

  > returns: [`JSONArray`] list including number of prize levels and the min and max for instant win or `nil` for sweeps.


### User Profile Functions

For any of the following functions which require the user parameters, you may leave the parameters blank if the user information is already in place. Only the first call requiring the user's information requires the user parameters. User parameters are key/value pairs.

* #### isAuthenticated 
  Check if a user has been authenticated by the server.

  usage: `[prizeSDK isAuthenticated];`
  
  > returns: [`Boolean`] `true` if a user is authenticated, otherwise `false`

* #### setUserParams 
  Sets the user parameter array, but does not check the values.

  usage: `prizeSDK.setUserParams(userParameters);`
   
  > params: 
  >  * `userParameters` - set the user parameter JSONObject

* #### authenticateOnServer 
  Authenticates the user on the server

  usage: 
```java
	if (prizeSDK.authenticateOnServer(userParameters)) {
		Log.i(TAG, "authenticateOnServer");
	} else {
		Log.i(TAG, "not authenticateOnServer");
	}
```
  
  > params: 
  >  * `userParameters` - if not already set, JSONOBject of user parameter at least all required fields
  >
  > returns:[`Boolean`] `true` if user is successfully authenticated
  

* #### getCurrentProfileParams 
  Returns the current parameters in the user's profile as key value pairs

  usage: `prizeSDK.getCurrentProfileParams();`
  
  > returns: [`JSONArray`] keys and values currently set in the user's profiles


* #### getUserProfile 
  Returns the current user profile object with all possible profile fields and their current values

  usage: `prizeSDK.getUserProfile();`
  
  > params: 
  >  * `userParameters` - if not already set, JSONOBject of user parameter at least all required fields
  >
  > returns: [`JSONArray`] fields after server update, each field with name, value, and is_required
 

* #### setUserProfile 
  Updates or sets the User Profile on the server.

  usage: 
	```java
	  	JSONObject profile_fields = new JSONObject();
		try {
			profile_fields.put("city", "calgary");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		prizeSDK.setUserProfile(profile_fields, null);
	```
  
  > params: 
  >  * `profile_fields` - JSONOBject of all fields to update
  >  * `userParameters` - if not already set, a user parameter array containing at minimum, all required fields
 

### Entries and Plays Functions

* #### canEnter 
  Checks to see if the user is currently permitted to play / enter (use either canEnter or nextPlay - not both)

  usage: `[prizeSDK.canPlay(userParameters);`
  
  > params: 
  >  * `userParameters` - if not already set, JSONOBject of user parameter at least all required fields
  >  * `error` - pointer to `NSError`
  >
  > returns: [`Boolean`] returns YES if the user is allowed to play


* #### nextPlay 
  Checks when the user can play next. (use either canEnter or nextPlay - not both) 

  usage: `prizeSDK.nextPlay(userParameters);`
  
  > params: 
  >  * `userParameters` - if not already set, JSONOBject of user parameter at least all required fields
  >
  > returns: [`String`] Formatted date of when a user can play next. '0' in the case that a user can play now.


* #### enterSweeps 
  Enter the sweepstakes

  usage: `[prizeSDK enterSweeps:userParameters error:&error];`
  
  > params: 
  >  * `userParameters` - if not already set, JSONOBject of user parameter at least all required fields
  >  * `error` - pointer to `NSError`
  >
  > returns: [`NSDictionary`] A game object array containing gameID, result_text and other values


* #### enterInstantWin 
  Enter instant win

  usage: `prizeSDK.enterInstantWin(userParameters);`
  
  > params: 
  >  * `userParameters` - if not already set, JSONOBject of user parameter at least all required fields
  
  >
  > returns: [`JSONBObject`] A game object array containing gameID, result_text and other values
 

* #### buildAndSend 
  This method allows you to make an asynchronous call to the server with a specific action. It's the base method of peSDK. ViewController that instantiate peSDK will have to implement the `peSDKDelegate` and filter the response by the action string. This method is only required if synchronous calls are needed, otherwise the peSDK provides all the functionalities already.

 
  Possible actions are: 
     * `getconfig` - get contest configuration from server, this is called when peSDK is instantiated 
     * `auth_only` - check server authentication for `username`
     * `canplay` - check if a `username` can play the contest
     * `updateprofile` - update `username` fields
     * `gamehistory` -  get the game history for a specific `username`
     * `instantwin` - get a game result form the server

  usage: 
  
    ```java
    
	    @Override
		public void actionComplete(String action, JSONObject result) {
			if (action.equals("getconfig")) {
				configComplete();
			} else {
				Log.i(TAG, "actionComplete unknown action:" + action);
			}
		}
  
    ```
     > params: 
     >  * `userParameters` - if not already set, a user parameter array containing at minimum, all required fields
     >
     > returns: [`JSONObject`] An JSONObject of game info such as containing gameID, result_text and other values
 

### Other Classes

* peHTTPClient: wrapper for request model. Formats data for the request to the server.
* peConfig: fetches, caches and evaluates configuration variables for a promotion.
* peConstants: peSDK Android constant variables.
* peError: manages error levels and exceptions.

Multilingual games can be done using the localized NLS.properties file. The NLS.properties file contains all the possible responses from the server. You need to create a new NLS.properties file for each language you want to support. For examples to support French, you will need to create NLS_fr.properties with all values translated to French. Also refer to localization documentation for Android.

## Javadoc

Use ant to build javadoc for peSDK Android. Edit build.xml file and edit classpath and bootclasspath attribute. Specifically, edit `additionalparam="-bootclasspath /path/to/sdk/platforms/android-##/android.jar"" packagelistLoc="/path/to/sdk/docs/reference" />` and `<link offline="true" href="http://developer.android.com/reference/" packagelistLoc="/path/to/sdk/docs/reference" />` and any other jars that you get warning messages about

`ant javadoc`

## Contact

Feel free to contact me if you have any questions.

[Thieu Huynh](http://github.com/ThieuHuynh)
