/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.jrwz.server.utils;

import java.sql.Time;
import java.util.logging.Logger;

import l1j.jrwz.server.model.gametime.L1GameTime;

/**
 * 时间周期.
 */
public class TimePeriod {
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(TimePeriod.class.getName());

    private final Time _timeStart;
    private final Time _timeEnd;

    public TimePeriod(Time timeStart, Time timeEnd) {
        if (timeStart.equals(timeEnd)) {
            throw new IllegalArgumentException(
                    "timeBegin must not equals timeEnd");
        }

        _timeStart = timeStart;
        _timeEnd = timeEnd;
    }

    public boolean includes(L1GameTime time) {
        /*
         * 分かりづらいロジック・・・ timeStart after timeEndのとき(例:18:00~06:00)
         * timeEnd~timeStart(06:00~18:00)の範囲内でなければ、
         * timeStart~timeEnd(18:00~06:00)の範囲内と見なせる
         */
        return _timeStart.after(_timeEnd) ? !includes(time, _timeEnd,
                _timeStart) : includes(time, _timeStart, _timeEnd);
    }

    private boolean includes(L1GameTime time, Time timeStart, Time timeEnd) {
        Time when = time.toTime();
        return timeStart.compareTo(when) <= 0 && 0 < timeEnd.compareTo(when);
    }
}
