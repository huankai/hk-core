package com.hk.commons.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author kevin
 * @date 2019-7-19 16:07
 */
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Lazy<T> implements Supplier<T> {

    private final Supplier<T> supplier;

    private T value;

    private boolean resolved = false;

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    public static <T> Lazy<T> of(T value) {
        Assert.notNull(value, "Value must not be null!");
        return new Lazy<>(() -> value);
    }

    @Override
    public T get() {
        T value = getNullable();
        if (value == null) {
            throw new IllegalStateException("Expected lazy evaluation to yield a non-null value but got null!");
        }
        return value;
    }

    public Optional<T> getOptional() {
        return Optional.ofNullable(getNullable());
    }

    public Lazy<T> or(Supplier<? extends T> supplier) {
        Assert.notNull(supplier, "Supplier must not be null!");
        return Lazy.of(() -> orElseGet(supplier));
    }

    private T orElseGet(Supplier<? extends T> supplier) {
        Assert.notNull(supplier, "Default value supplier must not be null!");
        T value = getNullable();
        return value == null ? supplier.get() : value;
    }

    public <S> Lazy<S> map(Function<? super T, ? extends S> function) {
        Assert.notNull(function, "Function must not be null!");
        return Lazy.of(() -> function.apply(get()));
    }

    public <S> Lazy<S> flatMap(Function<? super T, Lazy<? extends S>> function) {
        Assert.notNull(function, "Function must not be null!");
        return Lazy.of(() -> function.apply(get()).get());
    }

    private T getNullable() {
        T value = this.value;
        if (this.resolved) {
            return value;
        }
        value = supplier.get();
        this.value = value;
        this.resolved = true;
        return value;
    }

}
