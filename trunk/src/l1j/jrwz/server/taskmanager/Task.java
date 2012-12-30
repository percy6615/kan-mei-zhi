/* This program is free software; you can redistribute it and/or modify
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
package l1j.jrwz.server.taskmanager;

import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.taskmanager.TaskManager.ExecutedTask;

/**
 * @author Layane
 * 
 */
public abstract class Task {
    private static Logger _log = Logger.getLogger(Task.class.getName());

    public abstract String getName();

    public void initializate() {
        if (Config.DEBUG) {
            _log.info("Task" + getName() + " inializate");
        }
    }

    public ScheduledFuture<?> launchSpecial(ExecutedTask instance) {
        return null;
    }

    public void onDestroy() {
    }

    public abstract void onTimeElapsed(ExecutedTask task);
}
