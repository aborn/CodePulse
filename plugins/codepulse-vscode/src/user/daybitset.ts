import { BitSet } from "../common/bitset";
import * as dateutils from "../utils/dateutils";
const SLOT_SIZE = 24 * 60 * 2;
const SLOT_SIZE_HOUR = 60 * 2;

export class DayBitSet {
    private day: string;
    private bitset: BitSet;

    constructor() {
        this.day = dateutils.getDayInfo();
        this.bitset = new BitSet(SLOT_SIZE);
    }

    public record(): number {
        this.clearIfNotToday();
        let slot = dateutils.getSlotIndex();
        this.bitset.set(slot);

        // console.log("slot=" + slot + ":" + this.bitset.get(slot) + ", cardinality=" + this.bitset.cardinality() + ", wordlength=" + this.bitset.wordLength());
        return slot;
    }

    public getSlotValue(): number {
        this.clearIfNotToday();
        let slot = dateutils.getSlotIndex();
        return this.bitset.get(slot);
    }

    public clearIfNotToday(): void {
        if (dateutils.isToday(this.day)) {
            return;
        }

        this.day = dateutils.getDayInfo();
        this.bitset.clear();
    }

    public getBitSet(): BitSet {
        return this.bitset;
    }

    public getDayBitSetByteArray(): number[] {
        return this.bitset.toIntArray();
    }

    public countOfCodingSlot(): number {
        return this.bitset.cardinality();
    }

    public print(): void {
        let dayStaticByHour = this.getDayStaticByHour();
        console.log(`day: ${this.day}, cardinality:${this.bitset.cardinality()}, dayStaticByHour: ${this.array2string(dayStaticByHour)}`);
    }

    public getDayStaticByHour(): number[] {
        let dayStaticByHour: number[] = [];
        for (let i = 0; i < SLOT_SIZE; i++) {
            if (this.bitset.get(i) === 1) {
                let ndx = Math.floor(i / SLOT_SIZE_HOUR);
                if (!dayStaticByHour[ndx]) {
                    dayStaticByHour[ndx] = 0;
                }
                dayStaticByHour[ndx] += 1;
            }
        }
        return dayStaticByHour;
    }

    private array2string(arr: number[]): string {
        let result = '[';
        let temp = [];
        for (let i = 0; i < arr.length; i++) {
            if (arr[i]) {
                temp[i] = i + ':' + arr[i];
            }
        }

        return result + temp.join(',') + ']';
    }

    public getDay(): string {
        return this.day;
    }
}