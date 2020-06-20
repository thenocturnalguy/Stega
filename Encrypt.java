import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

class Encrypt {

	private static int OFFSET;

	Encrypt(int OFFSET) { this.OFFSET = OFFSET;	}

	public int get_offset() { return this.OFFSET; }

	public void set_offset(int OFFSET) { this.OFFSET = OFFSET; }

	private ArrayList<Integer> _int_to_binary(int unicode) {
		ArrayList<Integer> bits = new ArrayList<Integer>();
		
		try {			

			do { bits.add(0, unicode % 2); }
			while((unicode /= 2) > 0);

			while((8 - bits.size()) > 0) { bits.add(0, 0); }

		} catch(ArithmeticException ex) { ex.printStackTrace(); }
		catch(NumberFormatException ex) { ex.printStackTrace(); }
		finally { return bits; }
	} 

	private int _binary_to_int(ArrayList<Integer> pixels) {
		int pointer = 0, val = 0;
		try {
			for(int item:pixels) {
				val += pixels.get(pointer) * Math.pow(2, pixels.size() - pointer - 1);
				++pointer;
			}
			return val;
		} catch(IndexOutOfBoundsException ex) { ex.printStackTrace(); }
		finally { return val; }
	}

	private void _log_result(String result, FileOutputStream writer) {
		try {
			byte[] output_log = result.getBytes();
			writer.write(output_log);
		} catch(IOException ex ){ ex.printStackTrace(); }
	}

	private ArrayList<Integer> _merge_array(ArrayList<Integer> child_one, ArrayList<Integer> child_two) {

		child_one.addAll(child_two);

		return child_one;
	}

	private ArrayList<Integer> _flatten_array(String secret_message) {
		int ch = 0, message_len;
		ArrayList<Integer> bits;
		ArrayList<Integer> all_bits = new ArrayList<Integer>();

		message_len = secret_message.length();

		try {
			for(int index = 0; index < message_len; ++index) {
				ch = secret_message.charAt(index);

				bits = _int_to_binary(ch);
				
				all_bits = _merge_array(all_bits, bits);
			}
		} catch(StringIndexOutOfBoundsException ex){ ex.printStackTrace(); }
		finally { return all_bits; }
	}

	void encryptor(String img_name, String secret_message, boolean should_log) {
		try {
			ArrayList<Integer> pixels, all_bits;
			int unicode, byte_index = 0, bits_len, pointer = 0;
			String result = null;
			FileInputStream reader = null;
			FileOutputStream writer_log = null, writer_img = null;

			all_bits = _flatten_array(secret_message);
			bits_len = all_bits.size();

			reader = new FileInputStream(img_name);
			if(should_log) writer_log = new FileOutputStream("output.txt");
			writer_img = new FileOutputStream("output.bmp");

			while((unicode = reader.read()) != -1) {
				++byte_index;
				pixels = _int_to_binary(unicode);
				result = pixels + "\t";
				if(byte_index >= this.OFFSET && pointer < bits_len) {
					pixels.set(7, all_bits.get(pointer));
					result += unicode + "\t" + pixels + "\t";
					unicode = _binary_to_int(pixels);
					
					result += unicode + "\t" + byte_index + "\n";
					++pointer;
				} else {
					result += unicode + "\t" + byte_index + "\n";
				}

				if(should_log && writer_log != null) _log_result(result, writer_log);
				writer_img.write(unicode);
			}

			reader.close();
			if(writer_log != null) writer_log.close();
			writer_img.close();
		} catch(IOException ex) { ex.printStackTrace();	} 	
	}
}

