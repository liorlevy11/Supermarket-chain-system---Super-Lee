package BussinessLayer.Managers;

import BussinessLayer.Objects.Category;
import BussinessLayer.Objects.CategoryType;
import DataAccessLayer.Dal.CategoryDAO;

import java.util.List;

public class CategoryManager {
    private static CategoryManager categoryManager;
    private int IDCounter = 0;

    public int getIDCounter() {
        return IDCounter;
    }

    public int getIDAnd_ADD() {
        IDCounter++;
        return IDCounter - 1;
    }

    public void setIDCounter(int newID) {
        IDCounter = CategoryDAO.getInstance().getMaxID() + 1;
    }

    private CategoryManager() {
        IDCounter = CategoryDAO.getInstance().getMaxID() + 1;
    }

    public static CategoryManager getInstance() {
        if (categoryManager == null)
            categoryManager = new CategoryManager();
        return categoryManager;
    }

    public List<Category> getCategories() {

        return CategoryDAO.getInstance().getAllCategories();
    }

    public void loadData() {
        addCategory("Food", "main");

        addCategory("Dogs", "main");
        addCategory("Moti", "main");

        addCategory("kk", "sub");
        addCategory("Animal", "sub");
        addCategory("Ss", "sub");

        addCategory("3L", "subSub");
        addCategory("200 ml", "subSub");
        addCategory("150 kilo", "subSub");
    }

    public Category addCategory(String name, String categoryType) {
        Category category = null;
        if (!categoryType.equalsIgnoreCase("subsub") && !categoryType.equalsIgnoreCase("sub") && !categoryType.equalsIgnoreCase("main"))
            throw new IllegalArgumentException("The category type: " + categoryType + " is illegal. please choose one of the following: main,sub,subSub");
        switch (categoryType.toLowerCase()) {
            case "main":
                category = new Category(CategoryDAO.getInstance().getMaxID() + 1, name, CategoryType.Main, -1);
                CategoryDAO.getInstance().Insert(category);
                break;
            case "sub":
                category = new Category(CategoryDAO.getInstance().getMaxID() + 1, name, CategoryType.Sub, -1);
                CategoryDAO.getInstance().Insert(category);
                break;
            case "subsub":
                category = new Category(CategoryDAO.getInstance().getMaxID() + 1, name, CategoryType.SubSub, -1);
                CategoryDAO.getInstance().Insert(category);
        }
        return category;

    }

    public Category getCategory(int ID) {
        Category category = null;
        for (Category c : CategoryDAO.getInstance().getAllCategories()) {
            if (c.getID() == ID) {
                category = c;
            }
        }
        if (category == null)
            throw new IllegalArgumentException("this category dose not exist");
        return category;
    }
}
