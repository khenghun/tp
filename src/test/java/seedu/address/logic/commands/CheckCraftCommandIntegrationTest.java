package seedu.address.logic.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.LocationList;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.RecipeList;
import seedu.address.model.UserPrefs;
import seedu.address.model.item.Quantity;
import seedu.address.model.recipe.Ingredient;
import seedu.address.model.recipe.IngredientList;
import seedu.address.model.recipe.Recipe;
import seedu.address.testutil.RecipeBuilder;

import static seedu.address.logic.commands.CheckCraftCommand.MESSAGE_SUCCESS_CRAFTABLE;
import static seedu.address.logic.commands.CheckCraftCommand.MESSAGE_SUCCESS_UNCRAFTABLE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertInventoryCommandFailure;
import static seedu.address.testutil.TypicalItems.APPLE;
import static seedu.address.testutil.TypicalItems.BANANA;
import static seedu.address.testutil.TypicalItems.getTypicalItemList;

public class CheckCraftCommandIntegrationTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalItemList(), new LocationList(), new RecipeList(), new UserPrefs());
        IngredientList ingredients = new IngredientList();
        ingredients.add(new Ingredient(1, 3));
        // recipe to create 2 apples from 3 bananas
        Recipe recipe = new RecipeBuilder().withId(1).withIngredients(ingredients).withQuantity("2")
                .withDescription("test").build();
        model.addRecipe(recipe);
        expectedModel = new ModelManager(model.getItemList(), model.getLocationList(),
                model.getRecipeList(), new UserPrefs());
    }

    @Test
    public void execute_craftable_success() {
        CheckCraftCommand ccc = new CheckCraftCommand(APPLE.getName(), new Quantity("3"));
        String expectedMessage = String.format(MESSAGE_SUCCESS_CRAFTABLE, APPLE.getName()) +
                "\ntest: Banana[6] -> Apple[4]\n";
        assertCommandSuccess(ccc, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_uncraftable_success() {
        CheckCraftCommand ccc = new CheckCraftCommand(APPLE.getName(), new Quantity("200"));
        String expectedMessage = String.format(MESSAGE_SUCCESS_UNCRAFTABLE, APPLE.getName());
        assertCommandSuccess(ccc, model, expectedMessage, expectedModel);
    }

    /**
     * Tests for failure when quantity is zero.
     */
    @Test
    public void execute_invalidQuantity_throwsCommandException() {
        CheckCraftCommand ccc = new CheckCraftCommand(APPLE.getName(), new Quantity("0"));
        String expectedMessage = CheckCraftCommand.MESSAGE_INVALID_QUANTITY;
        assertInventoryCommandFailure(ccc, model, expectedMessage);
    }

    /**
     * Tests for failure when item cannot be found in item list.
     */
    @Test
    public void execute_invalidItem_throwsCommandException() {
        CheckCraftCommand ccc = new CheckCraftCommand("apple2", new Quantity("1"));
        String expectedMessage = CheckCraftCommand.MESSAGE_ITEM_NOT_FOUND;
        assertInventoryCommandFailure(ccc, model, expectedMessage);
    }

    /**
     * Tests for failure when no recipe to craft the item can be found
     */
    @Test
    public void execute_recipeNotFound_throwsCommandException() {
        CheckCraftCommand ccc = new CheckCraftCommand(BANANA.getName(), new Quantity("1"));
        String expectedMessage = CraftItemCommand.MESSAGE_RECIPE_NOT_FOUND;
        assertInventoryCommandFailure(ccc, model, expectedMessage);
    }
}