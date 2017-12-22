package com.piccante.anurr.piccantemenu;

/**
 * Created by anurr on 9/25/2017.
 */

public class TabMessage {
    public static String get(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {
            case R.id.tab_order:
                message += "nearby";
                break;
            case R.id.tab_list:
                message += "favorites";
                break;
            case R.id.tab_history:
                message += "booking";
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }
}
