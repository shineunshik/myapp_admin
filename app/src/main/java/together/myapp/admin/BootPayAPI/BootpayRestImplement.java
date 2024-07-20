package together.myapp.admin.BootPayAPI;

import kr.co.bootpay.model.response.data.TokenData;

public interface BootpayRestImplement {

    @Deprecated
    void callbackRestToken(TokenData acessToken);

    @Deprecated
    void callbackEasyPayUserToken(EasyPayUserTokenData userToken);

}
