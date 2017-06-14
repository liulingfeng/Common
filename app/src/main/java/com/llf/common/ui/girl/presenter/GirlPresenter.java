package com.llf.common.ui.girl.presenter;

import android.content.Context;

import com.llf.common.data.local.JcodeLocalDataSource;
import com.llf.common.entity.JcodeEntity;
import com.llf.common.ui.girl.contract.GirlContract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by llf on 2017/3/28.
 * 发现Presenter页
 */

public class GirlPresenter implements GirlContract.Presenter {
    private GirlContract.View view;
    private List<JcodeEntity> jcodes = new ArrayList<>();
    private JcodeLocalDataSource mJcodeLocalDataSource;

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
                        return getJcodes(s);
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
                .subscribe(new Subscriber<List<JcodeEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorTip("您已进入没有网络的二次元");
                    }

                    @Override
                    public void onNext(List<JcodeEntity> jcodeEntities) {
                        view.returnData(jcodeEntities);
                    }
                });
    }

    @Override
    public void addRecord(Context context, final JcodeEntity entity) {
        mJcodeLocalDataSource = JcodeLocalDataSource.getInstance(context);
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(mJcodeLocalDataSource.saveJcode(entity) == 0 ? false : true);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorTip("添加发现出错");
                    }

                    @Override
                    public void onNext(Boolean result) {

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
            view.showErrorTip("jsoup解析出错");
        }
        return jcodes;
    }
}
