package tr.com.novasta.novasta;

class Item {
    int id;

    boolean isReference;
    boolean isBlog;
    boolean isPage;

    String title;
    String description;
    String categoryName;
    String date;
    String image;

    public Item(int ID, String TITLE, String DESCRIPTION, String DATE, String CATEGORY_NAME, String IMAGE, int IS_WHAT) {
        isReference = false;
        isBlog = false;
        isPage = false;
        categoryName = CATEGORY_NAME;

        switch (IS_WHAT) {
            case 0:
                isReference = true;
                break;
            case 1:
                isBlog = true;
                break;
            case 2:
                isPage = true;
                break;
        }

        id = ID;
        title = clib.decode(TITLE);
        description = clib.decode(DESCRIPTION);
        image = clib.decode(IMAGE);
        if (DATE.length() > 8) {
            date = new cproject().date(clib.decode(DATE).substring(0, DATE.length() - 8));
        }
    }
}
