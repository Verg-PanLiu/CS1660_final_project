import java.io.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
  
public class top_n_Mapper extends Mapper<Object,
                              Text, LongWritable, Text> {
  
    @Override
    public void map(Object key, Text value, 
       Context context) throws IOException, 
                      InterruptedException
    {
  
        String[] tokens = value.toString().split("\t");
  
        String name = tokens[0];
        long number = Long.parseLong(tokens[1]);
  
        number = (-1) * number;
  
        context.write(new LongWritable(number),
                              new Text(name));
    }
}