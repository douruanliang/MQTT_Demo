package io.dourl.mqtt.model.message.event;


import io.dourl.mqtt.base.BaseObject;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.model.BaseUser;

/**
 * 好友状态更新消息
 * Created by dourl on 16/3/3.
 */
public class UserUpdateMessage extends MessageModel {

    /**
     * type : user_update
     * obj : {"id":"100156","username":"pzhangone","sessionIcon":"http://7xqgm2.com2.z0.glb.qiniucdn.com/avatar/7025a3290f0bd64182dc3390197c16a1.jpg","cover":["http://7xqgm2.com2.z0.glb.qiniucdn.com/usercover/default-6.png"],"fullname":"pzhangone","intro":"我是谁","description":"[{\"color\":\"#3ea3f9\",\"title\":\"我是深蓝色\"},{\"color\":\"#3ea3f9\",\"title\":\"深蓝色的我\"},{\"color\":\"#3ea3f9\",\"title\":\"蓝色\"}]","country":"中国","province":"北京市","country_code":"CN","city_code":"10","city":"北京市","email":"dourl@nihao.com","mobile":"18910481681","sex":"1","birth_day":"1990/06/15","age":"25","truthful":"3","currency":"1000000.00","money":"0.00","follows":"1","fans":"1","unionid":"","formatted_address":"北京市 朝阳区"}
     */

    private EventEntity event;

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public static class EventEntity implements BaseObject {
        private String type;
        /**
         * id : 100156
         * username : pzhangone
         * sessionIcon : http://7xqgm2.com2.z0.glb.qiniucdn.com/avatar/7025a3290f0bd64182dc3390197c16a1.jpg
         * cover : ["http://7xqgm2.com2.z0.glb.qiniucdn.com/usercover/default-6.png"]
         * fullname : pzhangone
         * intro : 我是谁
         * description : [{"color":"#3ea3f9","title":"我是深蓝色"},{"color":"#3ea3f9","title":"深蓝色的我"},{"color":"#3ea3f9","title":"蓝色"}]
         * country : 中国
         * province : 北京市
         * country_code : CN
         * city_code : 10
         * city : 北京市
         * email : dourl@nihao.com
         * mobile : 18910481681
         * sex : 1
         * birth_day : 1990/06/15
         * age : 25
         * truthful : 3
         * currency : 1000000.00
         * money : 0.00
         * follows : 1
         * fans : 1
         * unionid :
         * formatted_address : 北京市 朝阳区
         */

        private BaseUser obj;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public BaseUser getObj() {
            return obj;
        }

        public void setObj(BaseUser obj) {
            this.obj = obj;
        }

    }
}
