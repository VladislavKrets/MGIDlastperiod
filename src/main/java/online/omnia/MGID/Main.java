package online.omnia.MGID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lollipop on 28.10.2017.
 */
public class Main {
    public static int days;
    public static long deltaTime = 24 * 60 * 60 * 1000;

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            return;
        }
        if (!args[0].matches("\\d+")) return;
        if (Integer.parseInt(args[0]) == 0) {
            deltaTime = 0;
        }
        days = Integer.parseInt(args[0]);

        List<AccountsEntity> accountsEntities = MySQLDaoImpl.getInstance().getAccountsEntities("MGID");
        System.out.println(accountsEntities);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(JsonTokenEntity.class, new JsonTokenDeserializer());
        builder.registerTypeAdapter(List.class, new JsonCampaignListDeserializer());
        Gson gson = builder.create();
        String answer;
        List<JsonCampaignEntity> jsonCampaignEntities = null;
        Map<String, JsonSourceEntity> jsonSourceEntityMap = null;
        SourceStatisticsEntity sourceStatisticsEntity;
        Map<String, JsonTeaser> jsonTeaserMap;
        Map<String, String> parameters;
        int length = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SourceStatisticsEntity entity;
        int count = 0;
        int afid = 0;
        String url = null;
        for (AccountsEntity accountsEntity : accountsEntities) {
           for (int i = 0; i <= days; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread.sleep(205);
                answer = HttpMethodUtils.getMethod("http://api.mgid.com/v1/goodhits/clients/"
                        + accountsEntity.getClientsId() /*"141484"*/ + "/campaigns?token=" + accountsEntity.getApiKey()/*"b35913ab2499b95dbf09325a69c97b5d"*/);
                while (count < 10) {
                    try {
                        jsonCampaignEntities = gson.fromJson(answer, List.class);
                        break;
                    } catch (Exception e) {
                        System.out.println("waiting for 1 sec");
                        try {
                            count++;
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (count == 10) {
                    count = 0;
                    Utils.write("http://api.mgid.com/v1/goodhits/clients/"
                            + accountsEntity.getClientsId() + "/campaigns?token=" + accountsEntity.getApiKey());
                    continue;
                }
                count = 0;
                for (JsonCampaignEntity jsonCampaignEntity : jsonCampaignEntities) {
                    Thread.sleep(205);
                    answer = HttpMethodUtils.getMethod("http://api.mgid.com/v1/goodhits/clients/"
                            + accountsEntity.getClientsId()
                            +"/teasers?token="
                            + accountsEntity.getApiKey()
                            + "&fields=[%27url%27]&campaign="
                            + jsonCampaignEntity.getId());
                    while (count < 10) {
                        try {
                            jsonTeaserMap = gson.fromJson(answer, new TypeToken<Map<String, JsonTeaser>>() {
                            }.getType());
                            for (Map.Entry<String, JsonTeaser> entry : jsonTeaserMap.entrySet()) {
                                url = entry.getValue().getUrl();
                                break;
                            }
                            parameters = Utils.getUrlParameters(url);
                            if (parameters.containsKey("t9")) {
                                if (parameters.get("t9").matches("\\d+")
                                        && MySQLDaoImpl.getInstance().getAffiliateByAfid(Integer.parseInt(parameters.get("t9"))) != null) {
                                    afid = Integer.parseInt(parameters.get("t9"));
                                } else {
                                    afid = 0;
                                }
                            } else afid = 2;

                            break;
                        } catch (JsonSyntaxException e) {
                            System.out.println("waiting for 1 sec");
                            try {
                                count++;
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(205);
                    answer = HttpMethodUtils.getMethod("http://api.mgid.com/v1/goodhits/campaigns/"
                            + jsonCampaignEntity.getId() +
                            "/quality-analysis/uid?token=" + accountsEntity.getApiKey()/*"b35913ab2499b95dbf09325a69c97b5d*/ + "&dateInterval=interval&startDate="
                            + dateFormat.format(new Date(System.currentTimeMillis() - deltaTime - i * 24L * 60 * 60 * 1000))
                            + "&endDate="
                            + dateFormat.format(new Date(System.currentTimeMillis() - deltaTime - i * 24L * 60 * 60 * 1000)));
                    answer = answer.replaceAll("\\{\"\\d+\":\\{\"\\d+-\\d+-\\d+_\\d+-\\d+-\\d+\":", "");
                    length = answer.length();
                    answer = answer.substring(0, length - 2);
                    System.out.println(answer);

                    while (count < 10) {
                        try {
                            jsonSourceEntityMap = gson.fromJson(answer, new TypeToken<Map<String, JsonSourceEntity>>() {
                            }.getType());
                            break;
                        } catch (JsonSyntaxException e) {
                            System.out.println("waiting for 1 sec");
                            try {
                                count++;
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    if (count == 10) {
                        count = 0;
                        Utils.write("http://api.mgid.com/v1/goodhits/campaigns/"
                                + jsonCampaignEntity.getId() +
                                "/quality-analysis/uid?token=" + accountsEntity.getApiKey() + "&dateInterval=interval&startDate="
                                + dateFormat.format(new Date(System.currentTimeMillis() - deltaTime - i * 24L * 60 * 60 * 1000))
                                + "&endDate="
                                + dateFormat.format(new Date(System.currentTimeMillis() - deltaTime - i * 24L * 60 * 60 * 1000)));
                        continue;
                    }
                    count = 0;

                    for (Map.Entry<String, JsonSourceEntity> entry : jsonSourceEntityMap.entrySet()) {
                        if (entry.getValue().getClicks() == 0 && entry.getValue().getCpc() == 0 && entry.getValue().getSpent() == 0) continue;
                        sourceStatisticsEntity = new SourceStatisticsEntity();
                        sourceStatisticsEntity.setAdsetId(entry.getKey());
                        sourceStatisticsEntity.setCampaignId(String.valueOf(jsonCampaignEntity.getId()));
                        sourceStatisticsEntity.setCampaignName(jsonCampaignEntity.getName());
                        sourceStatisticsEntity.setReceiver("API");
                        sourceStatisticsEntity.setSpent(entry.getValue().getSpent());
                        sourceStatisticsEntity.setClicks(entry.getValue().getClicks());
                        sourceStatisticsEntity.setCpc(entry.getValue().getCpc());
                        sourceStatisticsEntity.setAccount_id(accountsEntity.getAccountId());
                        sourceStatisticsEntity.setAfid(afid);
                        sourceStatisticsEntity.setBuyerId(accountsEntity.getBuyerId());
                        sourceStatisticsEntity.setDate(new java.sql.Date(System.currentTimeMillis() - deltaTime - i * 24L * 60 * 60 * 1000));

                        if (Main.days != 0) {
                            entity = MySQLDaoImpl.getInstance().getSourceStatistics(sourceStatisticsEntity.getAccount_id(),
                                    sourceStatisticsEntity.getCampaignName(), sourceStatisticsEntity.getDate(), sourceStatisticsEntity.getAdsetId());
                            if (entity != null) {
                                sourceStatisticsEntity.setId(entity.getId());
                                MySQLDaoImpl.getInstance().updateSourceStatistics(sourceStatisticsEntity);
                                entity = null;
                            } else MySQLDaoImpl.getInstance().addSourceStatistics(sourceStatisticsEntity);
                        } else {
                            if (MySQLDaoImpl.getInstance().isDateInTodayAdsets(sourceStatisticsEntity.getDate(), sourceStatisticsEntity.getAccount_id(),
                                    sourceStatisticsEntity.getCampaignId(), sourceStatisticsEntity.getAdsetId())) {
                                MySQLDaoImpl.getInstance().updateTodayAdset(Utils.getAdset(sourceStatisticsEntity));
                            } else MySQLDaoImpl.getInstance().addTodayAdset(Utils.getAdset(sourceStatisticsEntity));

                        }
                    }
                }
            }
        }
        MySQLDaoImpl.getSessionFactory().close();
    }
}
