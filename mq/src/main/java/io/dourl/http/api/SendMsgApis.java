package io.dourl.http.api;

import io.dourl.http.callback.CallAdapterFactory;
import io.dourl.http.model.BaseResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/3/17
 */
public interface SendMsgApis {
    @FormUrlEncoded
    @POST(CommandInterface.IM_MESSAGE)
    CallAdapterFactory.ImCall<BaseResponse> sendMsg(@Field("uid") String uid, @Field("type") int type, @Field("body") String body);


    @FormUrlEncoded
    @POST(CommandInterface.IM_GROUP_MESSAGE)
    CallAdapterFactory.ImCall<BaseResponse> sendGroupMsg(@Field("gid") String uid, @Field("type") int type, @Field("body") String body);
}
