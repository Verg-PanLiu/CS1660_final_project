import java.io.IOException;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.HashMap;
import org.apache.hadoop.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class InvertedIndexV2 {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, Text>{
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String filename = ((FileSplit) context.getInputSplit()).getPath().getName();
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken().replaceAll("[^a-zA-Z]", "").toLowerCase());
        context.write(word, new Text(filename));
      }
    }
  }

  public static class IntSumReducer
       extends Reducer<Text,Text,Text,Text> {

    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
      HashMap filemap = new HashMap();
      int sum = 0;
      for(Text f: values){
        String name = f.toString();

        if(filemap != null && filemap.get(name) != null){
          sum = (int)filemap.get(name);
          filemap.put(name, ++sum);
        }else{
          filemap.put(name, 1);
        }
      }
      context.write(key, new Text(filemap.toString()));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setNumReduceTasks(1);
    job.setJarByClass(InvertedIndexV2.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}