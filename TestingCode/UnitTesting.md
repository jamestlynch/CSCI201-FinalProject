Unit Testing

TEST CASE #1: JSON File Parser, Valid Input
Test that the JSON file parser is correctly parsing the data. Use "assertequals" statements to test the following variables:
-updated_cars: Test that updated_cars is being incremented for each car in the JSON file
-idnum: Test that idnum is set with the correct car id#
-one_car id: Check that idnum was successfully converted from a String to an int and that one_car id is set to the correct integer value
 -speedval: Test that the car speed from the JSON file is stored correctly in the speedval variable
 -one_car speed: Check that speedval was successfully converted from a String to a double and that one_car speed is set to the correct double value
 directonval: Check that directionval is set with the correct String value from the JSON file
 -CarDirection: check that CarDirection is set to the correct corresponding direction (NORTH, SOUTH, EAST, WEST) based on the direction given by the JSON file
 -ramp: Check that ramp is correctly set to the String "on/off ramp"
 -rampname: Test that rampname is set with the correct String value from the JSON file
 -freeway: Check that freeway is correctly set to the String "freeway" from the input given from the JSON file
 -one_car freeway: Check that one_car freeway is correctly set to the freewaysegment returned by the searchForSegment method
