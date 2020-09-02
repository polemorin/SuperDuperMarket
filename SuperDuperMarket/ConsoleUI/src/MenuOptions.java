public enum MenuOptions {
    LOADXML("1) Load xml file.",1),
    SHOW_STORE_DETAILS("2) Show store details.",2),
    SHOW_ALL_PRODUCTS("3) Show all products in system.",3),
    MAKE_ORDER("4) Place order.",4),
    SHOW_ORDER_HISTORY("5) Show orders made in Super Duper Market.",5),
    UPDATE_STORE_INVENTORY("6) Update store inventory.", 6),
    SAVE_ORDER_HISTORY_TO_FILE("7) Save order history to file.",7),
    LOAD_ORDER_HISTORY_TO_FILE("8) Load order history from file.",8),
    EXIT("9) Exit.",9);

    private final String name;
    private final int menuOption;

    MenuOptions(String name,int optionNum){
        this.name = name;
        this.menuOption = optionNum;
    }
    public static void printMenuOptions(){
        System.out.println();
        System.out.println("*****Main menu******");
        for (MenuOptions option: MenuOptions.values()) {
            System.out.println(option);
        }
    }
    @Override
    public String toString() {
        return name;
    }
    public static boolean isOptionValid(int menuOption){
        return menuOption <= EXIT.menuOption && menuOption > 0;
    }
    public int getOptionNumber(){return this.menuOption;}
}
