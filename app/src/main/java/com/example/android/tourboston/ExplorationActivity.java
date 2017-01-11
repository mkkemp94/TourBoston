package com.example.android.tourboston;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.tourboston.MainActivity.username;

public class ExplorationActivity extends AppCompatActivity {

    // Lists of attractions
    public static ArrayList<Attraction> attractionListFood;
    public static ArrayList<Attraction> attractionListSchools;
    public static ArrayList<Attraction> attractionListPlaces;

    // Layout for nav drawer, view for list, plus a toggle
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    // Title of the activity (to change on open nav drawer)
    private String mActivityTitle;

    // Holds fragments to show on swipe
    private ViewPager viewPager;

    //------------------------------------------
    //------------------------------------------

    /**
     * ONCREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploration);

        // Swap custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Populate each array list of attractions
        populateFoodAttractions();
        populateSchoolAttractions();
        populatePlaceAttractions();

        // Allow multiple fragments in activity swipe
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        // Get the ORIGINAL activity name
        mActivityTitle = getTitle().toString();

        // Set up drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_drawer);
        if (mNavigationView != null) {
            setupDrawerContent();
        }

        // Shows arrow at the top left
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set up floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareAttractionLists();
            }
        });
    }

    /**
     * Share attraction lists via mail,
     * Depending on which tab is open.
     */
    private void shareAttractionLists() {

        String showAttractionNames = "";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this cool app!");

        switch (viewPager.getCurrentItem()){
            case 0: showAttractionNames = showAttractionNames(attractionListFood);
                break;
            case 1: showAttractionNames = showAttractionNames(attractionListSchools);
                break;
            case 2: showAttractionNames = showAttractionNames(attractionListPlaces);
                break;
            default: break;
        }

        intent.putExtra(Intent.EXTRA_TEXT, "The name is TourBoston. It has information on: " +
                "\n" + showAttractionNames +
                "\n\n" + "... and more!");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Gets the names of all attractions in the list for this tsb
     * @param list of attractions
     * @return
     */
    private String showAttractionNames(ArrayList<Attraction> list) {

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < list.size(); i++) {
            stringBuffer.append("\n" + list.get(i).getAttractionName());
        }

        return stringBuffer.toString();
    }

    /**
     * Fill view pager with fragments
     * Use an Adapter (custom)
     * Assign view pager to tab layout
     */
    private void setupViewPager(ViewPager viewPager) {

        // Add fragments
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new AttractionListFragment(), "Foods");
        adapter.addFragment(new AttractionListFragment(), "Schools");
        adapter.addFragment(new AttractionListFragment(), "Places");
        viewPager.setAdapter(adapter);

        // Set up tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.exploration_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Get the drawer layout
     * Toggle page title on open/close drawer
     * Set the hamburger menu icon
     */
    private void setupDrawerContent() {

        // Toggle page title on action
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (username == null) {
                    getSupportActionBar().setTitle("Hello, user");
                } else {
                    getSupportActionBar().setTitle("Hello, " + username);
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

                // Focus on draw so click
                mNavigationView.bringToFront();
                mDrawerLayout.requestLayout();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }
        };

        // Show hamburger menu, attach toggle object to drawer layout
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // Click listener
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.navigation_item_1) {
                            Intent intent = new Intent(ExplorationActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.navigation_sub_item_1){
                            viewPager.setCurrentItem(0);
                        } else if (menuItem.getItemId() == R.id.navigation_sub_item_2) {
                            viewPager.setCurrentItem(1);
                        } else if (menuItem.getItemId() == R.id.navigation_sub_item_3) {
                            viewPager.setCurrentItem(2);
                        }

                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    /**
     * Populated the array list holding the food attractions.
     * This is called from on create so thst it only happens once.
     */
    private void populateFoodAttractions() {
        attractionListFood = new ArrayList<Attraction>();
        attractionListFood.add(new Attraction("Il Mondos", R.drawable.image_pizza,
                R.drawable.il_mondos_bg, "Mostly Italian food. Huge pizza slices!"));
        attractionListFood.add(new Attraction("Five Guys", R.drawable.image_pizza,
                R.drawable.five_guys_bg, "Awesome fries. And burgers. And malted milk shakes."));
        attractionListFood.add(new Attraction("Uno", R.drawable.image_pizza,
                R.drawable.uno_bg, "Not the best customer service or food."));
        attractionListFood.add(new Attraction("Wendy's", R.drawable.image_pizza,
                R.drawable.wendys_bg, "Fast food at it's (finest?)"));
        attractionListFood.add(new Attraction("Qdoba", R.drawable.image_pizza,
                R.drawable.qdoba_bg, "Mexican food. Great burritos."));
        attractionListFood.add(new Attraction("Terryaki House", R.drawable.image_pizza,
                R.drawable.teriyaki_house_bg, "Rice bowls and bubble tea."));
        attractionListFood.add(new Attraction("JP Licks", R.drawable.image_pizza,
                R.drawable.jp_licks_bg, "Ice creeeeam!"));
        attractionListFood.add(new Attraction("Boston Market", R.drawable.image_pizza,
                R.drawable.boston_market_bg, "Kinda homemade food? Not the best imo."));
        attractionListFood.add(new Attraction("Starbucks", R.drawable.image_pizza,
                R.drawable.starbucks_bg, "Koffee! Staaaarbucks!"));
        attractionListFood.add(new Attraction("Boloco", R.drawable.image_pizza,
                R.drawable.boloco_bg, "More mexican food. Also cheeseburger wraps."));
        attractionListFood.add(new Attraction("Tremont House of Pizza", R.drawable.image_pizza,
                R.drawable.tremont_bg, "Great service. Not the best food."));
        attractionListFood.add(new Attraction("Cheesecake Factory", R.drawable.image_pizza,
                R.drawable.cheesecake_factory_bg, "Expensive. Food's pretty good though."));
        attractionListFood.add(new Attraction("5 Napkin Burger", R.drawable.image_pizza,
                R.drawable.five_napkin_bg, "Also expensive. Unnecessarily."));
        attractionListFood.add(new Attraction("Subway", R.drawable.image_pizza,
                R.drawable.subway_bg, "Eat fresh."));
        attractionListFood.add(new Attraction("Dunkin Donuts", R.drawable.image_pizza,
                R.drawable.dunkin_donuts_bg, "Egg and cheese croissants. And coffee."));
        attractionListFood.add(new Attraction("Chipotle", R.drawable.image_pizza,
                R.drawable.chipotle_bg, "Another mexican food place."));
    }

    /**
     * Populated the array list holding the school attractions.
     * This is called from on create so thst it only happens once.
     */
    private void populateSchoolAttractions() {
        attractionListSchools = new ArrayList<Attraction>();
        attractionListSchools.add(new Attraction("Wentworth", R.drawable.image_school,
                R.drawable.wentworth_bg, "Go leopards!"));
        attractionListSchools.add(new Attraction("MassArt", R.drawable.image_school,
                R.drawable.massart_bg, "Secondary education institution. Smelly liberal arts college."));
        attractionListSchools.add(new Attraction("MCPHS", R.drawable.image_school,
                R.drawable.mcphs_bg, "Well, they have a library."));
        attractionListSchools.add(new Attraction("Wheelock", R.drawable.image_school,
                R.drawable.wheelock_bg, "Home of the Hobo Hole."));
        attractionListSchools.add(new Attraction("Emmanuel", R.drawable.image_school,
                R.drawable.emmanuel_bg, "Buffet."));
        attractionListSchools.add(new Attraction("Northeastern University", R.drawable.image_school,
                R.drawable.northeastern_bg, "Large and in charge."));
        attractionListSchools.add(new Attraction("Simmons", R.drawable.image_school,
                R.drawable.simmons_bg, "Where is that exactly?"));
        attractionListSchools.add(new Attraction("Boston University", R.drawable.image_school,
                R.drawable.boston_university_bg, "A college."));
        attractionListSchools.add(new Attraction("Berklee", R.drawable.image_school,
                R.drawable.berklee_bg, "Laaaaaa."));
    }

    /**
     * Populated the array list holding the places attractions.
     * This is called from on create so thst it only happens once.
     */
    private void populatePlaceAttractions() {
        attractionListPlaces = new ArrayList<Attraction>();
        attractionListPlaces.add(new Attraction("Emerald Necklace", R.drawable.image_place,
                R.drawable.emerald_necklace, "Love is in the air."));
        attractionListPlaces.add(new Attraction("Mission Hill", R.drawable.image_place,
                R.drawable.mission_hill, "Pretty okay place."));
        attractionListPlaces.add(new Attraction("Boston Common", R.drawable.image_place,
                R.drawable.common_bg, "Don't come here at night."));
        attractionListPlaces.add(new Attraction("Prudential Center", R.drawable.image_place,
                R.drawable.prudential, "Everything is expensive."));
        attractionListPlaces.add(new Attraction("Olmstead Park", R.drawable.image_place,
                R.drawable.olmstead_park, "Pretty pond. And more love."));
        attractionListPlaces.add(new Attraction("Museum of Fine Arts", R.drawable.image_place,
                R.drawable.museum_of_fine_arts, "Respectable, once you get to know it."));
        attractionListPlaces.add(new Attraction("Boston Public Gardens", R.drawable.image_place,
                R.drawable.public_garden, "Swan boats."));
        attractionListPlaces.add(new Attraction("Isabella Stewart Gardeener Museum", R.drawable.image_place,
                R.drawable.isabella_stewart, "One lady. Many items."));
    }

    /**
     * Shows three bars and stuff (and switch icons quicker)
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * Faster title change
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                menu.findItem(R.id.menu_night_mode_system).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                menu.findItem(R.id.menu_night_mode_auto).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                menu.findItem(R.id.menu_night_mode_night).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                menu.findItem(R.id.menu_night_mode_day).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // When sandwich icon is clicked, inform system
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_night_mode_system:
                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case R.id.menu_night_mode_day:
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.menu_night_mode_night:
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.menu_night_mode_auto:
                setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        }
    }

    /**
     * Class to give fragments to view pager
     */
    static class Adapter extends FragmentPagerAdapter {

        // Variables
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        // Constructor
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        // Adds fragments to list for view pager
        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        // Get fragment based on position
        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();

            if (position == 0) {
                bundle.putString("List", "Food");
            } else if (position == 1) {
                bundle.putString("List", "Schools");
            } else {
                bundle.putString("List", "Places");
            }

            Fragment fragment = mFragments.get(position);
            fragment.setArguments(bundle);
            return fragment;
        }

        // Get count of fragment list
        @Override
        public int getCount() {
            return mFragments.size();
        }

        // Get page title based on position
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
