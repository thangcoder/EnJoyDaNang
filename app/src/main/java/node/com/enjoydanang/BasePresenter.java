package node.com.enjoydanang;

import node.com.enjoydanang.api.ApiStores;
import node.com.enjoydanang.api.module.AppClient;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by WuXiaolong
 * on 2015/9/23.
 * github:https://github.com/WuXiaolong/
 * weibo:http://weibo.com/u/2175011601
 * 微信公众号：吴小龙同学
 * 个人博客：http://wuxiaolong.me/
 */
public class BasePresenter<V> {
    public V mvpView;
    protected ApiStores apiStores;
    protected ApiStores apiStoresANS;
    protected ApiStores apiStoresAmazon;

    public BasePresenter(V view){
        attachView(view);
    }

    private CompositeSubscription mCompositeSubscription;
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        apiStores = AppClient.getClient().create(ApiStores.class);
    }


    public void detachView() {
//        this.mvpView = null;
        onUnsubscribe();
    }

    public void unbSubcriber() {
        onUnsubscribe();
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {

        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
