package com.llf.common.ui.girl;

import com.llf.basemodel.utils.LogUtil;
import com.llf.common.entity.JcodeEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by llf on 2017/3/28.
 */

public class GirlPresenter implements GirlContract.Presenter {
    private GirlContract.View view;
    private  List<JcodeEntity> jcodes = new ArrayList<>();

    public GirlPresenter(GirlContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadData(String url) {
        Observable.just(url)
                .map(new Func1<String, List<JcodeEntity>>() {
                    @Override
                    public List<JcodeEntity> call(String s) {
                        return  getJcodes(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
//                        view.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<JcodeEntity>>() {
                    @Override
                    public void call(List<JcodeEntity> jcodeEntities) {
//                        view.stopLoading();
                        view.returnData(jcodeEntities);
                    }
                });
    }

    private List<JcodeEntity> getJcodes(String url) {
        jcodes.clear();
        try {
            Document document = Jsoup.connect(url).get();
            Elements contents = document.select(".archive-item");
            for (Element e : contents) {
                JcodeEntity entity = new JcodeEntity();
                entity.setTitle(e.select("a[href]").attr("title"));
                entity.setDetailUrl(e.select("h3").select("a[href]").attr("href"));
                entity.setImgUrl(e.select(".covercon").select("img[src]").attr("src"));
                entity.setContent(e.select(".archive-detail").select("p").text());
                entity.setAuthor(e.select(".list-user").select("span").text());
                entity.setAuthorImg(e.select(".list-user").select("img").attr("src"));
                entity.setAuthorUrl(e.select(".list-user").select("a").attr("href"));
                Elements msg = e.select(".glyphicon-class");
                entity.setWatch(msg.get(0).text());
                entity.setComments(msg.get(1).text());
                entity.setLike(msg.get(2).text());
                jcodes.add(entity);
            }
        } catch (IOException e) {
            LogUtil.e("出错了");
        }
        return jcodes;
    }
}
