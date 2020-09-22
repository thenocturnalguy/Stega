package com.stega;  
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

class Decrypt {

	private static int OFFSET;

	Decrypt(int OFFSET) { this.OFFSET = OFFSET;	}

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

	private ArrayList<Integer> _merge_array(ArrayList<Integer> child_one, ArrayList<Integer> child_two) {

		child_one.addAll(child_two);

		return child_one;
	}

	private ArrayList<Integer> _flatten_array(String secret_message) {
		int ch = 0, message_len = secret_message.length();
		ArrayList<Integer> bits;
		ArrayList<Integer> all_bits = new ArrayList<Integer>();

		try {
			for(int index = 0; index < message_len; ++index) {
				ch = secret_message.charAt(index);

				bits = _int_to_binary(ch);
				
				all_bits = _merge_array(all_bits, bits);
			}
		} catch(StringIndexOutOfBoundsException ex){ ex.printStackTrace(); }
		finally { return all_bits; }
	}

	private boolean _isPresent(char[] arr, int unicode) {
		for(int i = 0; i < arr.length; ++i)
			if(unicode == arr[i]) return true;
		return false;
	}

	private boolean _isLetter(int unicode) {
		if((unicode >= 65 && unicode <= 90) || (unicode >= 97 && unicode <= 122)) return true;
		else return false;
	}

	private boolean _isNumber(int unicode) {
		if(unicode >= 48 && unicode <= 57) return true;
		else return false;
	}

	private boolean _shouldTerminate(int unicode) {
		char special_chars[] = {
			'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '+', '=', '/', '{', '}', '[', ']', '<', '>', ',', '.', '?', ':', ';', '\'', '\"', '\n', '\t', ' ' 
		};

		if(_isLetter(unicode) || _isNumber(unicode) || _isPresent(special_chars, unicode)) return false;
		else return true;
	}

	String decryptor(String img_name) {

		ArrayList<Integer> pixels, last_bits = new ArrayList<Integer>();
		int unicode, byte_index = 0, hidden_code;
		String result = null, secret_message = "";

		try {		

			FileInputStream reader = new FileInputStream(img_name);
			
			reader.skip(this.OFFSET - 1);

			while((unicode = reader.read()) != -1) {

				if(byte_index == 8) {
					byte_index = 0;
					hidden_code = _binary_to_int(last_bits);
					if(_shouldTerminate(hidden_code)) break;
					secret_message += (char)hidden_code;
					last_bits.clear();
				}
				
				pixels = _int_to_binary(unicode);
				last_bits.add(pixels.get(7));

				++byte_index;
			}

			reader.close();
			
		} catch(IOException ex) { ex.printStackTrace();	} 
		finally { return secret_message; }	
	}
}
