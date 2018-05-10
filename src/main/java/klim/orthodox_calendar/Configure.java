package klim.orthodox_calendar;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Ignore;

@Entity
public class Configure {
        @Id
        private Long id;

        private Integer daysForward;
        private Integer daysToStore;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Integer getDaysForward() {
                return daysForward;
        }

        public void setDaysForward(Integer daysForward) {
                this.daysForward = daysForward;
        }

        public Integer getDaysToStore() {
                return daysToStore;
        }

        public void setDaysToStore(Integer daysToStore) {
                this.daysToStore = daysToStore;
        }

        public Configure(int forward, int store) {
                this.daysForward = forward;
                this.daysToStore = store;
        }

        public Configure() {
        	this(4, 14);
    }
}

