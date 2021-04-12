
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.ViewPagerActions.scrollLeft;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.IsNull.notNullValue;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.ViewPagerActions.scrollRight;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;
    private NeighbourApiService mApiService;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mApiService = DI.getNeighbourApiService() ;
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(Matchers.allOf(ViewMatchers.withId(R.id.list_neighbours),isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(Matchers.allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(Matchers.allOf(ViewMatchers.withId(R.id.list_neighbours),isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(Matchers.allOf(ViewMatchers.withId(R.id.list_neighbours),isDisplayed())).check(withItemCount(ITEMS_COUNT-1));
    }



    //When we click an item, the Profile Page is launching
    @Test
    public void checkIsNeighbourProfileActivityLaunched() {
        // clique sur  caroline
        onView(Matchers.allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // on test que l'activity MyNeighbourProfile est lancee
        onView(withId(R.id.Activity_vue)).check(matches(isDisplayed()));
    }



    /**
     * The FavList's size == number of Favorites Neighbours
     */
    @Test
    public void myFavoritesList_haveOnlyFavorites()
    {
        //Given : The Favorite List is empty
        onView(ViewMatchers.withId(R.id.container)).perform(scrollRight());
        mApiService.getFavoriteNeighbours().clear();
        onView(Matchers.allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(0));

        onView(ViewMatchers.withId(R.id.container)).perform(scrollLeft());

        //Ajouter 4 favoris dans la liste et check
        for(int i = 0; i < 4; i++)
        {
            mApiService.changeNeighbourFavoriteStatus(mApiService.getNeighbours().get(i));
        }
        onView(Matchers.allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Espresso.pressBack();
        onView(ViewMatchers.withId(R.id.container)).perform(scrollRight());


        //Liste des favoris avec 4
        onView(Matchers.allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(4));
    }

    /**
     * When we click on a Neighbour, give the good Neighbour's name
     */
    @Test
    public void activityVue_loadNameText_shouldBeTheGoodOne()
    {
        // clique sur  caroline
        onView(Matchers.allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // on test que le bon nom apparait
        onView(withId(R.id.prenom)).check(matches(withText("Caroline")));
    }
}