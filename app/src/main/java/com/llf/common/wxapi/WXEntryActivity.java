package com.llf.common.wxapi;

import android.content.Intent;
import com.llf.basemodel.base.BaseActivity;
import com.llf.common.R;
import com.llf.common.constant.AppConfig;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by llf on 2017/4/26.
 * 微信分享回调
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wxenter;
    }

    @Override
    protected void initView() {
        iwxapi = WXAPIFactory.createWXAPI(this, AppConfig.APP_ID_WEIXIN, false);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(com.tencent.mm.sdk.openapi.BaseReq baseReq) {

    }

    @Override
    public void onResp(com.tencent.mm.sdk.openapi.BaseResp baseResp) {
        String result;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }
        showToast(result);
        finish();
    }
}
