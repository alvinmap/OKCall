package com.lzx.okcall.library.rx;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lzx.okcall.library.info.Response;
import com.lzx.okcall.library.Utils;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * json 数据转换
 * create by lzx
 * time:2018/8/23
 */
public class ResultTransformer<T> implements ObservableTransformer<Response, T>, FlowableTransformer<Response, T>,
        SingleTransformer<Response, T>, MaybeTransformer<Response, T> {

    private Class<T> clazz;
    private Gson gson;
    private TypeAdapter<T> adapter;

    public ResultTransformer(Class<T> clazz) {
        Utils.checkNotNull(clazz, "clazz is null");
        this.clazz = clazz;
        if (this.gson == null) {
            this.gson = new Gson();
        }
    }

    @Override
    public ObservableSource<T> apply(Observable<Response> upstream) {
        return upstream.flatMap(new Function<Response, ObservableSource<T>>() {
            @Override
            public ObservableSource<T> apply(Response response) throws Exception {
                ResponseBody value = response.body();
                Utils.checkNotNull(value, "response body is null");
                if (clazz == String.class) {
                    final String json = value.string();
                    return new ObservableSource<T>() {
                        @Override
                        public void subscribe(Observer<? super T> observer) {
                            observer.onNext((T) json);
                        }
                    };
                } else {
                    adapter = gson.getAdapter(TypeToken.get(clazz));
                    JsonReader jsonReader = gson.newJsonReader(value.charStream());
                    final T result = adapter.read(jsonReader);
                    value.close();
                    return new ObservableSource<T>() {
                        @Override
                        public void subscribe(Observer<? super T> observer) {
                            observer.onNext(result);
                        }
                    };
                }
            }
        });
    }

    @Override
    public Publisher<T> apply(Flowable<Response> upstream) {
        return upstream.flatMap(new Function<Response, Publisher<? extends T>>() {
            @Override
            public Publisher<? extends T> apply(Response response) throws Exception {
                ResponseBody value = response.body();
                Utils.checkNotNull(value, "response body is null");
                if (clazz == String.class) {
                    final String json = value.string();
                    return new Publisher<T>() {
                        @Override
                        public void subscribe(Subscriber<? super T> s) {
                            s.onNext((T) json);
                        }
                    };
                } else {
                    adapter = gson.getAdapter(TypeToken.get(clazz));
                    JsonReader jsonReader = gson.newJsonReader(value.charStream());
                    final T result = adapter.read(jsonReader);
                    value.close();
                    return new Publisher<T>() {
                        @Override
                        public void subscribe(Subscriber<? super T> s) {
                            s.onNext(result);
                        }
                    };
                }
            }
        });
    }

    @Override
    public MaybeSource<T> apply(Maybe<Response> upstream) {
        return upstream.flatMap(new Function<Response, MaybeSource<? extends T>>() {
            @Override
            public MaybeSource<? extends T> apply(Response response) throws Exception {
                ResponseBody value = response.body();
                Utils.checkNotNull(value, "response body is null");
                if (clazz == String.class) {
                    final String json = value.string();
                    return new MaybeSource<T>() {
                        @Override
                        public void subscribe(MaybeObserver<? super T> observer) {
                            observer.onSuccess((T) json);
                        }
                    };
                } else {
                    adapter = gson.getAdapter(TypeToken.get(clazz));
                    JsonReader jsonReader = gson.newJsonReader(value.charStream());
                    final T result = adapter.read(jsonReader);
                    value.close();
                    return new MaybeSource<T>() {
                        @Override
                        public void subscribe(MaybeObserver<? super T> observer) {
                            observer.onSuccess(result);
                        }
                    };
                }
            }
        });
    }

    @Override
    public SingleSource<T> apply(Single<Response> upstream) {
        return upstream.flatMap(new Function<Response, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(Response response) throws Exception {
                ResponseBody value = response.body();
                Utils.checkNotNull(value, "response body is null");
                if (clazz == String.class) {
                    final String json = value.string();
                    return new SingleSource<T>() {
                        @Override
                        public void subscribe(SingleObserver<? super T> observer) {
                            observer.onSuccess((T) json);
                        }
                    };
                } else {
                    adapter = gson.getAdapter(TypeToken.get(clazz));
                    JsonReader jsonReader = gson.newJsonReader(value.charStream());
                    final T result = adapter.read(jsonReader);
                    value.close();
                    return new SingleSource<T>() {
                        @Override
                        public void subscribe(SingleObserver<? super T> observer) {
                            observer.onSuccess(result);
                        }
                    };
                }
            }
        });
    }
}


