Blackbox testing

1. Function Testing
	TEST CASE #1: Regular GUI
		When the application is run, it is expected that the freeways are drawn on the map and the cars appear on the freeway, colored differently according to the speed at which they are travelling.  The colors should range from dark red to dark green and should always appear to be moving, unless colored dark red (speed = 0 miles per hour).
	TEST CASE #2: Zoom Feature
		Repeatedly zoom in and zoom out by pressing the '+' and '-' buttons on the map.  It is expected that the freeways remain properly drawn and cars are still visible on the freeways.  The car images should be scaled accordingly when zooming in and zooming out. 
	TEST CASE #3: Cars stick to freeways
		On the map, click and slide right and left, up and down.  It is expected that the cars remain on the freeways even if the view shifts.
	TEST CASE #4: Fastest Path
		Select a random source and a random destination from the pull-down menus.  Click the "Go" button and a time (in hours or minutes) should appear next to the label 'Time of Travel: '.  When the fastest path is drawn on the map, it is expected that the freeways remain in their proper locations, and the cars also remain on the map. 
	TEST CASE #5: Chart
		Click on the tabbed pane labelled 'Charts.'  Based on the source and destination information given by the 'Fastest Path' tab, it is expected that a chart displays with two columns: 'Depart Time' and 'Travel Time.'  The times in the 'Depart Time' column should range from 12am-11pm and you should be able to scroll through the table.
	TEST CASE #6: Export functionality
		Click on the "Export" button on the bottom of the 'Chart' tab.  Using Excel or some other spreadsheet, import the CSV file that is saved, to ensure it imports properly.
	TEST CASE #7: Graph
		Click on the tabbed pane labelled 'Graph'.  Based on the source and destination information given by the 'Fastest Path' tab, it is expected that a graph is displayed, illustrating the data from the chart in the 'Chart' tab. 
	