package lab2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.String;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.example.tobias.lab2.R;

public class MainActivity extends ActionBarActivity {

	ExpandableListView expandableListView;
	MyExpandableListAdapter myExpandableListAdapter;
	List<String> groupList;
	HashMap<String, List<String>> childMap;

	EditText pathText;
	int lastOpenedPos = -1; // Has to be an int because we keep track of which position the last opened has.
	int savedFlatPos = -1;

	View mostRecentMarked = null;
	String mostRecentSearch = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize the lists etc.
		init();

		expandableListView = (ExpandableListView) findViewById(R.id.mylist);
		myExpandableListAdapter = new MyExpandableListAdapter(this, groupList, childMap);
		expandableListView.setAdapter(myExpandableListAdapter);

		// Initialize all of the listeners.
		initListeners();
	}

	private void init() {
		groupList = new ArrayList<String>();
		childMap = new HashMap<String, List<String>>();

		// The text field.
		pathText = (EditText)findViewById(R.id.pathEdit);

		List<String> groupList0 = new ArrayList<String>();
		groupList0.add("Brown");
		groupList0.add("Brownish");
		groupList0.add("Red");
		groupList0.add("Red");

		List<String> groupList1 = new ArrayList<String>();
		groupList1.add("Cyan");
		groupList1.add("Magenta");
		groupList1.add("Yellow");
		groupList1.add("Black");

		List<String> groupList2 = new ArrayList<String>();
		groupList2.add("Green");
		groupList2.add("Pink");
		groupList2.add("Red");
		groupList2.add("Blue");

		groupList.add("Light");
		groupList.add("Medium");
		groupList.add("Dark");

		childMap.put(groupList.get(0), groupList0);
		childMap.put(groupList.get(1), groupList1);
		childMap.put(groupList.get(2), groupList2);
	}


	private void initListeners() {

		// Listens to if the text in the text field (EditText) has changed.
		pathText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				// Look for matches in every group and their children.
				for(int i = 0; i < myExpandableListAdapter.getGroupCount(); i++){

					for(int j = 0; j < myExpandableListAdapter.getChildrenCount(i); j++){

						// Add the group and child name into a string with the same pattern as in the edit text.
						String tempCompare = myExpandableListAdapter.getGroup(i).toString().toLowerCase() + "/" + myExpandableListAdapter.getChild(i,j).toString().toLowerCase();

						// Compares the two strings for an exact match.
						if(tempCompare.equals(pathText.getText().toString().toLowerCase())){
							pathText.setBackgroundColor(Color.WHITE); // Resets the text field background if it was red.



							// Expand the matched group.
							expandableListView.expandGroup(i);

							// Get the packed position for the child.
							long packedPos = expandableListView.getPackedPositionForChild(i, j);
							int flatPos = expandableListView.getFlatListPosition(packedPos);
							savedFlatPos = flatPos;
							expandableListView.setItemChecked(flatPos,true); // The item at the flat list position is set to true. Which is the new state in itemselector.xml.

							return;
						}

						// If text can become a full match, don't mark it red.
						if (tempCompare.startsWith(pathText.getText().toString().toLowerCase())){
							pathText.setBackgroundColor(Color.WHITE); // Resets the text field background if it was red.
							return;
						}
					}
				}

				// If we get here, no match was found. Set text background red (if it's not the standard input).
				if(!pathText.getText().equals("€/€")){
					pathText.setBackgroundColor(Color.RED);
					// If the input in the text field can't match anything in the tree. Remove the highlight from the last marked item.
					expandableListView.setItemChecked(savedFlatPos, false);
				}
			}
		});

		// Listens to when a key is pressed. In this case "enter"-key.
		pathText.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {

					// Fetch the user input from the EditText.
					String textInput = pathText.getText().toString().toLowerCase();

					// If the input matches a group's name then expand it.
					for (int i = 0; i < myExpandableListAdapter.getGroupCount(); i++) {
						if (textInput.equals(myExpandableListAdapter.getGroup(i).toString().toLowerCase())) {
							expandableListView.expandGroup(i);
							mostRecentSearch = textInput;

							return false;    // If a match is found then don't run the rest.
						}
					}

					// If the leaving text field is blank/standard then set the standard text and close the open tabs.
					if (textInput.equals("") || textInput.equals("€/€")) {
						for (int i = 0; i < myExpandableListAdapter.getGroupCount(); i++) {

							if (expandableListView.isGroupExpanded(i)) {    // Built in function in expandableListView.
								expandableListView.collapseGroup(i);
							}
							pathText.setText("€/€");
							pathText.setBackgroundColor(Color.WHITE);
						}
					}
				}
				return false;
			}
		});

		// Listens if a group is expanded.
		expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				// If it's not the first time, and if it's not the same position.
				if (lastOpenedPos != -1 && groupPosition != lastOpenedPos) {
					expandableListView.collapseGroup(lastOpenedPos);
				}
				lastOpenedPos = groupPosition; // Keep track of which was the last opened position.
			}
		});

		// Listens if a group is collapsed.
		expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {

				// Reset the color of the view for the most recent selected child.
				if (mostRecentMarked != null) {
					mostRecentMarked.setBackgroundColor(Color.WHITE);
				}
				// To prevent a different flat position to be highlighted when a new group is opened.
				if (savedFlatPos != -1) {
					expandableListView.setItemChecked(savedFlatPos, false);
				}
				// If no tabs are open, reset the path in the text field.
				for (int i = 0; i < myExpandableListAdapter.getGroupCount(); i++) {
					// If a group is expanded, return.
					if (expandableListView.isGroupExpanded(i)) {
						return;
					}
				}
				pathText.setText("€/€");
				pathText.setBackgroundColor(Color.WHITE);
			}
		});

		// Listens if a child is clicked on.
		expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				// Reset the color of the view for the most recent selected child.
				if (mostRecentMarked != null) {
					mostRecentMarked.setBackgroundColor(Color.WHITE);
				}

				mostRecentMarked = v;

				// Reset the text field if it's red.
				pathText.setBackgroundColor(Color.WHITE);

				// Get the names of the path items.
				String pathParent = myExpandableListAdapter.getGroup(groupPosition).toString();
				String pathChild = myExpandableListAdapter.getChild(groupPosition, childPosition).toString();

				// Set path in the text field (EditText) when an item is clicked;
				EditText pathText = (EditText) findViewById(R.id.pathEdit);	// kanske ta bort?
				pathText.setText(pathParent + "/" + pathChild);

				// Get the packed position for the child. This is needed if there's for example 2 children with the same name in the same group.
				long packedPos = expandableListView.getPackedPositionForChild(groupPosition, childPosition);
				int flatPos = expandableListView.getFlatListPosition(packedPos);
				savedFlatPos = flatPos;
				expandableListView.setItemChecked(flatPos,true);

				return false;
			}
		});
	}
}
