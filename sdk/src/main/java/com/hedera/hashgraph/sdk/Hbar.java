package com.hedera.hashgraph.sdk;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

/**
 * Typesafe wrapper for values of hbar providing foolproof conversions to other denominations.
 * <p>
 * May be positive, negative or zero.
 */
public final class Hbar implements Comparable<Hbar> {
    /**
     * Singleton value representing zero hbar.
     */
    public static final Hbar ZERO = new Hbar(0);

    /**
     * Singleton value for the minimum (negative) value this wrapper may contain.
     */
    public static final Hbar MIN = new Hbar(-50_000_000_000L);

    /**
     * Singleton value for the maximum (positive) value this wrapper may contain.
     */
    public static final Hbar MAX = new Hbar(50_000_000_000L);

    private final long tinybar;

    /**
     * Wrap some amount of hbar.
     *
     * @param amount the amount in hbar, may be negative.
     * @throws HbarRangeException if the tinybar equivalent does not fit in a {@code long}.
     */
    public Hbar(long amount) {
        this(null, Hbar.from(amount, HbarUnit.Hbar).tinybar);
    }

    /**
     * Wrap a possibly fractional amount of hbar.
     * <p>
     * The equivalent amount in tinybar must be an integer and fit in a {@code long}
     * (64-bit signed integer) as that is required by the Hedera network.
     * <p>
     * E.g. 1.23456789 is a valid amount of hbar but 0.123456789 is not.
     *
     * @param amount the amount in hbar, may be fractional and/or negative.
     * @throws HbarRangeException if the tinybar equivalent is not an integer
     *                            or does not fit in a {@code long}.
     */
    public Hbar(BigDecimal amount) {
        this(null, Hbar.from(amount, HbarUnit.Hbar).tinybar);
    }

    // HACK: Weird first param to differentiate from the public constructor
    @SuppressWarnings("UnusedVariable")
    private Hbar(@Nullable Void v, long tinybar) {
        this.tinybar = tinybar;
    }

    /**
     * Calculate an hbar amount given a value and a unit to interpret
     * it as.
     * <p>
     * The equivalent amount in tinybar must fit in a {@code long} (64-bit signed integer)
     * as that is required by the Hedera network.
     *
     * @param amount the amount in the given unit, may be negative.
     * @param unit   the unit to multiply the amount by.
     * @return the calculated hbar value.
     * @throws HbarRangeException if the tinybar equivalent does not fit in a {@code long}.
     */
    public static Hbar from(long amount, HbarUnit unit) {
        try {
            return Hbar.fromTinybar(amount * unit.tinybar);
        } catch (ArithmeticException e) {
            throw new HbarRangeException(amount + " " + unit + " is out of range for Hbar", e);
        }
    }

    /**
     * Calculate an hbar amount given a value and a unit to interpret
     * it as.
     * <p>
     * The equivalent amount in tinybar must be an integer and fit in a {@code long}
     * (64-bit signed integer) as that is required by the Hedera network.
     * <p>
     * E.g. 1.234 is a valid amount in {@link HbarUnit#Millibar}
     * but 1.2345 is not as that is 1234.5 tinybar.
     *
     * @param amount the amount in the given unit, may be fractional and/or negative.
     * @param unit   the unit to multiply the amount by.
     * @return the calculated hbar value.
     * @throws HbarRangeException if the tinybar equivalent is not an integer
     *                            or does not fit in a {@code long}.
     */
    public static Hbar from(BigDecimal amount, HbarUnit unit) {
        BigInteger tinybar;

        if (unit == HbarUnit.Tinybar) {
            try {
                // longValueExact() does this operation internally
                tinybar = amount.toBigIntegerExact();
            } catch (ArithmeticException e) {
                throw new HbarRangeException("tinybar amount is not an integer: " + amount, e);
            }
        } else {
            BigDecimal tinybarDecimal = amount.multiply(new BigDecimal(unit.tinybar));

            try {
                tinybar = tinybarDecimal.toBigIntegerExact();
            } catch (ArithmeticException e) {
                throw new HbarRangeException("tinybar equivalent of " + amount + " "
                    + unit + " (" + tinybarDecimal + ") is not an integer", e);
            }
        }

        return new Hbar(null, tinybar.longValue());
    }

    /**
     * Wrap an amount of tinybar.
     *
     * @param amount the amount, in tinybar; may be negative.
     * @return the wrapped hbar value.
     */
    public static Hbar fromTinybar(long amount) {
        return new Hbar(null, amount);
    }

    /**
     * Convert the hbar value to a different unit; the result may be fractional.
     *
     * @param unit the unit to reinterpret the value as.
     * @return the reinterpreted value.
     */
    public BigDecimal as(HbarUnit unit) {
        if (unit == HbarUnit.Tinybar) {
            return new BigDecimal(tinybar);
        }

        return new BigDecimal(tinybar).divide(new BigDecimal(unit.tinybar), MathContext.UNLIMITED);
    }

    /**
     * Get the equivalent tinybar amount.
     */
    public long asTinybar() {
        return tinybar;
    }

    /**
     * Get a human-readable printout of this hbar value for debugging purposes.
     * <p>
     * Not meant to be shown to users (localization/pretty-printing are not implemented).
     * The output format is unspecified.
     */
    @Override
    public String toString() {
        return tinybar + " " + HbarUnit.Tinybar.getSymbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hbar hbar = (Hbar) o;

        return tinybar == hbar.tinybar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tinybar);
    }

    @Override
    public int compareTo(Hbar o) {
        return Long.compare(tinybar, o.tinybar);
    }
}