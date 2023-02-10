package com.github.aborn.codepulse.common.datatypes;

import com.github.aborn.codepulse.common.utils.CodePulseDateUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;

/**
 * 将一天分为 24*60*2 = 2880 个slot，每个slot代表30S，slot=true表示编程时间
 * 相当于一分种拆分成2个slot
 * @author aborn
 * @date 2023/02/10 10:00
 */
public class DayBitSet implements Serializable {
    private static final int SLOT_SIZE_HOUR = 60 * 2;
    private static final int SLOT_SIZE = 24 * SLOT_SIZE_HOUR;
    private static final int MAX_SLOT_INDEX = SLOT_SIZE - 1;

    BitSet codingBitSet = new BitSet(SLOT_SIZE);

    /**
     * 一年中的天，格式为 yyyy-MM-dd
     */
    String day;

    /**
     * 用户信息，默认为token，全局唯一
     */
    String uid;

    public DayBitSet() {
        this(new Date());
    }

    private DayBitSet(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.day = simpleDateFormat.format(date);
    }

    public DayBitSet(String day, byte[] dayBitSetArray, String uid) {
        this.codingBitSet = BitSet.valueOf(dayBitSetArray);
        this.day = day;
        this.uid = uid;
    }

    private BitSet valueOf(String codeInfo) {
        BitSet likeBit;
        int initValue = 0;
        if (StringUtils.isBlank(codeInfo)) {
            likeBit = BitSet.valueOf(new long[]{initValue});
        } else {
            String[] bitStrArr = codeInfo.split(",");
            long[] bitLong = new long[bitStrArr.length];
            for (int i = 0; i < bitStrArr.length; i++) {
                bitLong[i] = StringUtils.isBlank(bitStrArr[i]) ? 0L : Long.parseLong(bitStrArr[i]);
            }
            likeBit = BitSet.valueOf(bitLong);
        }
        return likeBit;
    }

    public String getLikeInfo() {
        long[] bitLong = this.codingBitSet.toLongArray();
        String[] bitStr = new String[bitLong.length];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bitLong.length; i++) {
            long bitV = bitLong[i];
            if (bitV == 0L) {
                bitStr[i] = "";
            } else {
                bitStr[i] = String.valueOf(bitLong[i]);
            }
        }
        return String.join(",", bitStr);
    }

    public boolean isToday() {
        return CodePulseDateUtils.getTodayDayInfo().equals(day);
    }

    /**
     * slot range [0,2880-1]
     *
     * @param slot
     */
    public void set(int slot) {
        this.codingBitSet.set(slot);
    }

    public boolean get(int slot) {
        return this.codingBitSet.get(slot);
    }

    public void or(BitSet bitSet) {
        this.codingBitSet.or(bitSet);
    }

    public void or(DayBitSet dayBitSet) {
        or(dayBitSet.getCodingBitSet());
    }

    public void setSlotByCurrentTime() {
        this.setSlotByTime(new Date());
    }

    public void setSlotByTime(Date date) {
        int slotIndex = getSlotIndex(date);
        this.set(slotIndex);
    }

    public int getSlotIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int index = hours * 60 * 2 + minutes * 2 + (seconds / 30);
//        if (index > MAX_SLOT_INDEX) {
//            throw new Exception("Out of range exception.");
//        }

        return index;
    }

    public int countOfCodingSlot() {
        return codingBitSet.cardinality();
    }

    public boolean isEmptySlot() {
        return this.countOfCodingSlot() <= 0;
    }

    /**
     * 这一天的编码时间 (单位S)
     *
     * @return
     */
    public int codingTimeSeconds() {
        return countOfCodingSlot() * 30;
    }

    /**
     * 这一天的写代码时间（单位分）
     *
     * @return
     */
    public double codingTimeMinutes() {
        return codingTimeSeconds() / 60.0;
    }

    @Override
    public String toString() {
        return "Day:" + day + ", Coding Time:" + codingTimeSeconds() + "s(" + codingTimeMinutes() + "m).";
    }

    public int[] getDayStaticByHour() {
        int[] dayStaticByHour = new int[24];

        for (int i = 0; i < SLOT_SIZE; i++) {
            if (this.codingBitSet.get(i)) {
                int index = i / SLOT_SIZE_HOUR;
                dayStaticByHour[index] += 1;
            }
        }

        return dayStaticByHour;
    }

    public void clearIfNotToday() {
        if (isToday()) {
            return;
        }

        this.day = CodePulseDateUtils.getTodayDayInfo();
        this.codingBitSet.clear();
    }

    public String getCurrentHourSlotInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return getHourSlotInfo(calendar.get(Calendar.HOUR_OF_DAY));
    }

    /**
     * 获取小时内的slot打印信息
     *
     * @param hour [0~23]
     * @return
     */
    public String getHourSlotInfo(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Calendar calendarSet = Calendar.getInstance();
        calendarSet.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE), hour, 0, 0);
        int index = calendarSet.get(Calendar.HOUR_OF_DAY) * 60 * 2;
        StringBuilder result = new StringBuilder(hour + "");
        result.append("[").append(index).append("+]:");

        int slotHourIndex = 0;
        for (int i = index; i < (index + 60 * 2); i++) {
            if (get(i)) {
                result.append(slotHourIndex).append(", ");
            }
            slotHourIndex++;
        }

        return result.toString();
    }

    public static void main(String[] args) {
        DayBitSet dayBitSet = new DayBitSet();
        dayBitSet.set(1);
        dayBitSet.set(2);
        dayBitSet.set(121);
        dayBitSet.setSlotByCurrentTime();

        int[] dayStaticByHour = dayBitSet.getDayStaticByHour();
        System.out.println(dayBitSet.toString());
        for (int i = 0; i < 24; i++) {
            System.out.println("a[" + i + "] = " + dayStaticByHour[i]);
        }

        System.out.println("[写]" + dayBitSet.toString());
        // System.out.println("数据保存结果：" + DataStoreManager.save(dayBitSet));
        // DayBitSet dayBitSetRead = DataStoreManager.read(UserManager.DEFAULT_TEST_UID_TOKEN, null);
        // System.out.println("[读]" + dayBitSetRead);

        System.out.println(dayBitSet.getCurrentHourSlotInfo());
    }

    public BitSet getCodingBitSet() {
        return codingBitSet;
    }

    public String getDay() {
        return day;
    }

    public String getUid() {
        return uid;
    }

    public byte[] getDayBitSetByteArray() {
        return this.codingBitSet.toByteArray();
    }
}
