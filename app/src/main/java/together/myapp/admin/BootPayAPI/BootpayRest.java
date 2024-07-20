package together.myapp.admin.BootPayAPI;

import android.content.Context;

import kr.co.bootpay.android.models.BootUser;

public class BootpayRest {

    private static DemoApiPresenter presenter;

    @Deprecated
    public static void getRestToken(Context context, BootpayRestImplement parent, String restApplicationId, String privateKey) {
        if (presenter == null) presenter = new DemoApiPresenter(new DemoApiService(context), parent);
        presenter.getRestToken(restApplicationId, privateKey);
    }

    @Deprecated
    public static void getEasyPayUserToken(Context context, BootpayRestImplement parent, String restToken, BootUser user) {
        if (presenter == null) presenter = new DemoApiPresenter(new DemoApiService(context), parent);
        presenter.getEasyPayUserToken(restToken, user);

    }

}
