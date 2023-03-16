public class Time {
    int hours;
    int minutes;

    public Time(){
        hours = 0;
        minutes = 0;
    }

    public Time(int minutes){
        hours = minutes / 60;
        this.minutes = minutes % 60;
    }

    public Time(String ts){
        int n = ts.length();
        if(ts.substring(n - 2).equals("AM")){
            hours = 0;
        }else{
            hours = 12;
        }
        hours += Integer.valueOf(ts.substring(0, ts.indexOf(":")));
        if(hours % 12 == 0) hours -= 12;
        minutes = Integer.valueOf(ts.substring(ts.indexOf(":") + 1, ts.indexOf(" ")));
    }

    public Time(int hours, int minutes){
        this.hours = hours;
        this.minutes = minutes;
    }

    public Time(Time t){
        hours = t.hours;
        minutes = t.minutes;
    }

    public void add(Time t){
        minutes += t.minutes;
        hours += t.hours + minutes / 60;
        minutes = minutes % 60;
    }

    public void add(int minutes){
        this.minutes += minutes;
        hours += this.minutes / 60;
        this.minutes = this.minutes % 60;
    }

    public boolean before(Time t){
        if(hours < t.hours) return true;
        if(hours > t.hours) return false;
        if(minutes < t.minutes) return true;
        return false;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Time t){
            return t.hours == hours && t.minutes == minutes;
        }
        return false;
    }

    @Override
    public String toString(){
        String hourString = String.valueOf(hours);
        if(hourString.length() == 1) hourString = "0" + hourString;
        String minuteString = String.valueOf(minutes);
        if(minuteString.length() == 1) minuteString = "0" + minuteString;
        return hourString + ":" + minuteString;
    }
}
