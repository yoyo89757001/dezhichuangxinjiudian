package megvii.testfacepass.pa.beans;

public class LogingBean {


    /**
     * data : {"name":"德智创新","address":""}
     * device_sn : f72746ae706705ac7f5b59190b8825a9
     * token : a2b10e60631f418a964684e57dd7118f
     * score : 80.0
     * alive : true
     * rabbitmq : {"host":"120.24.167.205","port":"5672","vhost":"jxdface","login":"jxdface","password":"jxdface@gshis.com","queue":"f72746ae706705ac7f5b59190b8825a9","exchange":{"name":"GemstarFaceExchange","type":"direct","passive":false,"durable":true,"auto_delete":false,"arguments":null}}
     * success : true
     * code : 0
     * message : 登录成功
     */

    private DataBean data;
    private String device_sn;
    private String token;
    private float score;
    private boolean alive;
    private RabbitmqBean rabbitmq;
    private boolean success;
    private int code;
    private String message;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getDevice_sn() {
        return device_sn;
    }

    public void setDevice_sn(String device_sn) {
        this.device_sn = device_sn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public RabbitmqBean getRabbitmq() {
        return rabbitmq;
    }

    public void setRabbitmq(RabbitmqBean rabbitmq) {
        this.rabbitmq = rabbitmq;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * name : 德智创新
         * address :
         */

        private String name;
        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class RabbitmqBean {
        /**
         * host : 120.24.167.205
         * port : 5672
         * vhost : jxdface
         * login : jxdface
         * password : jxdface@gshis.com
         * queue : f72746ae706705ac7f5b59190b8825a9
         * exchange : {"name":"GemstarFaceExchange","type":"direct","passive":false,"durable":true,"auto_delete":false,"arguments":null}
         */

        private String host;
        private int port;
        private String vhost;
        private String login;
        private String password;
        private String queue;
        private ExchangeBean exchange;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getVhost() {
            return vhost;
        }

        public void setVhost(String vhost) {
            this.vhost = vhost;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }

        public ExchangeBean getExchange() {
            return exchange;
        }

        public void setExchange(ExchangeBean exchange) {
            this.exchange = exchange;
        }

        public static class ExchangeBean {
            /**
             * name : GemstarFaceExchange
             * type : direct
             * passive : false
             * durable : true
             * auto_delete : false
             * arguments : null
             */

            private String name;
            private String type;
            private boolean passive;
            private boolean durable;
            private boolean auto_delete;
            private Object arguments;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public boolean isPassive() {
                return passive;
            }

            public void setPassive(boolean passive) {
                this.passive = passive;
            }

            public boolean isDurable() {
                return durable;
            }

            public void setDurable(boolean durable) {
                this.durable = durable;
            }

            public boolean isAuto_delete() {
                return auto_delete;
            }

            public void setAuto_delete(boolean auto_delete) {
                this.auto_delete = auto_delete;
            }

            public Object getArguments() {
                return arguments;
            }

            public void setArguments(Object arguments) {
                this.arguments = arguments;
            }

            @Override
            public String toString() {
                return "ExchangeBean{" +
                        "name='" + name + '\'' +
                        ", type='" + type + '\'' +
                        ", passive=" + passive +
                        ", durable=" + durable +
                        ", auto_delete=" + auto_delete +
                        ", arguments=" + arguments +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "RabbitmqBean{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", vhost='" + vhost + '\'' +
                    ", login='" + login + '\'' +
                    ", password='" + password + '\'' +
                    ", queue='" + queue + '\'' +
                    ", exchange=" + exchange +
                    '}';
        }
    }
}
