package io.dourl.http.exception;


/**
 * 包含常见错误
 * Created by dourl on 17/2/21.
 */
public class BeingApiException extends ApiException {

    private static final int INVALIDATE_TOKEN = 401;
    private static final int EXPIRE_TOKEN = 402;
    private static final int SIGN_ERROR = 403;
    private static final int FORCE_UPGRADE = 10011;
    private static final int CLOSE_TRADE_MARKET = 10315;
    public static final int ERROR_OVERDUE = 10292; //过期
    public static final int ERROR_NOLEFT = 10293; //抢完了
    public static final int EXCHANGE_SELL_OUT = 10221; //兑换已无剩余
    public static final int ERROR_AUTH_PHONE = 60001; //未绑定手机
    public static final int ERROR_HAVE_LESS = 60002; //持有币不足
    /**
     * 查看 media 时，时间不足
     */
    public static final int TIME_NOT_ENOUGH = 10311;

    public static final int NEED_BUY_VIP = 10294;// 需要购买会员
    public static final int VIP_EXPIRED = 10295;// 会员已经过期,需要购买会员
    public static final int ONE_TIME_TO_BUY = 10296;// 按单条现金支付时, 确认支付信息

    public static final int ERROR_NO_BALANCE = 10701; //拼单-余额不足
    public static final int ERROR_NO_TIME = 10702; //拼单-时间不足
    public static final int ERROR_AGENT_MIN_LIMIT = 11500;//触发代购宝申请代理最低金额限制

    public BeingApiException(int code, String message) {
        super(code, message);
    }

    /*public boolean needLogout() {
        return mCode == INVALIDATE_TOKEN || mCode == EXPIRE_TOKEN || mCode == FORCE_UPGRADE || mCode == SIGN_ERROR;
    }

    public boolean forceUpgrade() {
        return mCode == FORCE_UPGRADE;
    }

    public boolean closeTradeMarket() {
        return mCode == CLOSE_TRADE_MARKET;
    }*/
}
