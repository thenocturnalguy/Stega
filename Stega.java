public class Stega {

	public static void main(String[] args) {
		try{
			String PATH_TO_IMG = null, SECRET_MESSAGE = null;
			boolean SHOULD_LOG = false;
			String options[] = {
				"-e", "--encrypt", "-d", "--decrypt", "-p", "--path", "-m", "--message", "-o", "--offset", "-l", "--log", "-h", "--help"
			};
			int arglen, OFFSET;

			arglen = args.length;

			if(args[0].equals(options[0]) || args[0].equals(options[1])) {

				if(args[1].equals(options[4]) || args[1].equals(options[5])) PATH_TO_IMG = args[2];
				else {
					System.out.println("\n" + "Wrong options provided.");
					return;
				}

				if(args[3].equals(options[6]) || args[3].equals(options[7])) SECRET_MESSAGE = args[4];
				else {
					System.out.println("\n" + "Wrong options provided.");
					return;
				}

				if(args[5].equals(options[8]) || args[5].equals(options[9])) OFFSET = Integer.parseInt(args[6]);
				else {
					System.out.println("\n" + "Wrong options provided.");
					return;
				}

				if(arglen > 7 && (args[7].equals(options[10]) || args[7].equals(options[11])))
					SHOULD_LOG = true;

				Encrypt E = new Encrypt(OFFSET);

				E.encryptor(PATH_TO_IMG, SECRET_MESSAGE, SHOULD_LOG);

			} else if(args[0].equals(options[2]) || args[0].equals(options[3])) {
				if(args[1].equals(options[4]) || args[1].equals(options[5])) PATH_TO_IMG = args[2];
				else {
					System.out.println("\n" + "Wrong options provided.");
					return;
				}

				if(args[3].equals(options[8]) || args[3].equals(options[9])) OFFSET = Integer.parseInt(args[4]);
				else {
					System.out.println("\n" + "Wrong options provided.");
					return;
				}

				Decrypt E = new Decrypt(OFFSET);

				SECRET_MESSAGE = E.decryptor(PATH_TO_IMG);

				System.out.println("\nThe secret message is -> " + SECRET_MESSAGE);
			} else {
				System.out.println("\nusage: java Stega [options]");
				System.out.println("options: -e, --encrypt -> Encrpyt in image.");
				System.out.println("\t -d, --decrypt -> Decrypt from image.");
				System.out.println("\t -p, --path -> Path of image.");
				System.out.println("\t -m, --message -> Message to be encrypted.");
				System.out.println("\t -o, --offset -> Byte index from where encryption will start.");
				System.out.println("\t -l, --log -> Write encryption log to text file(optional).");
				System.out.println("\t -h, --help -> Displays available options.");
				System.out.println("\n(encryption): java Stega -e -p path/to/image -m \"Your secret message.\" -o 10240");
				System.out.println("(decryption): java Stega -d -p path/to/image -o 10240");
			}
		} catch(NumberFormatException ex) { ex.printStackTrace();	} 
		catch(ArrayIndexOutOfBoundsException ex) { System.out.println("\nWrong options provided."); }
		finally { 
			System.out.println();
			System.gc(); 
		}
	}
}