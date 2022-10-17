package DataAccessLayer.Dal;


import BussinessLayer.Objects.Category;
import BussinessLayer.Objects.CategoryType;


import java.sql.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CategoryDAO extends DAO {
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String CATEGORY_TYPE = "CATEGORY_TYPE";
    private static final String SALE_ID = "SALE_ID";
    private HashMap<Integer, Category> categoriesHashMap;
    private static CategoryDAO instance = null;

    public static CategoryDAO getInstance() {
        if (instance == null)
            instance = new CategoryDAO();
        return instance;
    }

    private CategoryDAO() {
        super("CATEGORIES");
        categoriesHashMap = new HashMap<>();
    }

    public void resetCache(){
        categoriesHashMap.clear();
    }

    //insert new category, without knowing the ID
    public int insertCategory(String Name, String TypeString, int SaleId) {
        int id = getMaxID() + 1;
        Category cat = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt = c.createStatement();
            stmt = c.createStatement();
            String sql = "INSERT INTO " + DBHandler.CATEGORIES + " (" + ID + "," + NAME + "," + CATEGORY_TYPE + "," + SALE_ID + ") " +
                    "VALUES (" + id + ", '" + Name + "' ,'" + TypeString + "','" + SaleId + ");";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            CategoryType CType = null;
            if (TypeString.equals("Main"))
                CType = CategoryType.Main;
            else if (TypeString.equals("Sub")) {
                CType = CategoryType.Sub;
            } else {
                CType = CategoryType.SubSub;
            }
            categoriesHashMap.putIfAbsent(id, new Category(id, Name, CType, SaleId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public boolean Insert(Object categoryObj) {
        Category category = (Category) categoryObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}) VALUES(?, ?, ?, ?) "
                , _tableName, ID, NAME, CATEGORY_TYPE, SALE_ID
        );
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:SuperLee.db");
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, category.getID());
            pstmt.setString(2, category.getName());
            pstmt.setString(3, category.getCategoryTypeString());
            pstmt.setInt(4, category.getSale_ID());
            pstmt.execute();
            if (!categoriesHashMap.containsKey(category.getID())) {
                categoriesHashMap.putIfAbsent(category.getID(), category);
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    @Override
    public boolean Delete(Object categoryObj) {
        Category category = (Category) categoryObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ?"
                , _tableName, ID);

        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, category.getID());
            pstmt.executeUpdate();
            if (categoriesHashMap.containsKey(category.getID())) {
                categoriesHashMap.remove(category.getID());
            }


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public List<Category> selectAllCategories() {
        List<Category> list = (List<Category>) (List<?>) Select();
        return list;
    }


    public Category convertReaderToObject(ResultSet rs) throws SQLException {
        Category category = null;
        if (rs.getString(3).equals("Main")) {
            category = new Category(rs.getInt(1), rs.getString(2), CategoryType.Main, rs.getInt(4));
        } else if (rs.getString(3).equals("Sub")) {
            category = new Category(rs.getInt(1), rs.getString(2), CategoryType.Sub, rs.getInt(4));
        } else {
            category = new Category(rs.getInt(1), rs.getString(2), CategoryType.SubSub, rs.getInt(4));
        }
        if (!categoriesHashMap.containsKey(category.getID())) {
            categoriesHashMap.putIfAbsent(category.getID(), category);
        }

        return category;
    }

    public Category getCategoryById(int id) {
        if (categoriesHashMap.containsKey(id))
            return categoriesHashMap.get(id);
        Category category = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.CATEGORIES + " WHERE " + ID + " = " + id + ";");

            if (rs.next()) {
                int cId = rs.getInt(ID);
                String name = rs.getString(NAME);
                String CATtype = rs.getString(CATEGORY_TYPE);
                int SaleID = rs.getInt(SALE_ID);
                CategoryType CType = null;
                if (CATtype.equals("Main"))
                    CType = CategoryType.Main;
                else if (CATtype.equals("Sub")) {
                    CType = CategoryType.Sub;
                } else {
                    CType = CategoryType.SubSub;
                }
                category = new Category(cId, name, CType, SaleID);
                categoriesHashMap.put(id, category);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.CATEGORIES + ";");
            while (rs.next()) {
                int cId = rs.getInt(ID);
                String name = rs.getString(NAME);
                String CATtype = rs.getString(CATEGORY_TYPE);
                int SaleID = rs.getInt(SALE_ID);
                CategoryType CType = null;
                if (CATtype.equals("Main"))
                    CType = CategoryType.Main;
                else if (CATtype.equals("Sub")) {
                    CType = CategoryType.Sub;
                } else {
                    CType = CategoryType.SubSub;
                }
                Category category = new Category(cId, name, CType, SaleID);
                categories.add(category);
                categoriesHashMap.putIfAbsent(cId, category);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    public int getMaxID() {
        int MaxId = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.CATEGORIES + ";");
            while (rs.next()) {
                int cId = rs.getInt(ID);
                if (cId > MaxId) {
                    MaxId = cId;
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MaxId;
    }

    public int CountCategories() {
        int counter = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.CATEGORIES + ";");
            while (rs.next()) {
                counter++;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }

    public List<Category> getCategoriesbyNamesOrIds(List<String> names) {
        List<Category> categories = new LinkedList<>();
        for (String name : names) {
            try {
                int id = Integer.parseInt(name);
                categories.add(getCategoryById(id));
            } catch (Exception e) {
                categories.add(getCategoryByName(name));
            }
        }
        return categories;
    }

    public Category getCategoryByName(String name) {
        Category category = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.CATEGORIES + " WHERE " + NAME + " = '" + name + "';");

            if (rs.next()) {
                int cId = rs.getInt(ID);
                String CATtype = rs.getString(CATEGORY_TYPE);
                int SaleID = rs.getInt(SALE_ID);
                CategoryType CType = null;
                if (CATtype.equals("Main"))
                    CType = CategoryType.Main;
                else if (CATtype.equals("Sub")) {
                    CType = CategoryType.Sub;
                } else {
                    CType = CategoryType.SubSub;
                }
                category = new Category(cId, name, CType, SaleID);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category;
    }
}
