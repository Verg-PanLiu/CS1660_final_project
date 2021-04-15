import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
  
public class top_n_Reducer extends Reducer<LongWritable,
                            Text, LongWritable, Text> {
  
    static int count;
  
    @Override
    public void setup(Context context) throws IOException,
                                     InterruptedException
    {
  
        Configuration conf = context.getConfiguration();
  
        // we will use the value passed in myValue at runtime
        String param = conf.get("myValue");
  
        // converting the String value to integer
        count = Integer.parseInt(param);
    }
  
    @Override
    public void reduce(LongWritable key, Iterable<Text> values,
     Context context) throws IOException, InterruptedException
    {
  
        long number = (-1) * key.get();
        String name = null;
  
        for (Text val : values) {
            name = val.toString();
        }
  
        if (count > 0)
        {
            context.write(new LongWritable(number),
                                  new Text(name));
            count--;
        }
    }
}