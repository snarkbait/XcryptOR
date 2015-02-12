// BitFlag.java
// for Bit Manipulation tutorial
// by Snarkbait
// http://www.endangeredsouls.com/blog
package snarkbait;

public class BitFlag
{

	private int flags; // This is where we store all the flags


	// this array is for the overridden toString() function
	// quick and easy way to get enum name from given index
	private final static String[] flagNameArray = {"32BIT","64BIT","128BIT","DELETE_CRYPT","KEY_PHRASE","KEY_RAND","NO_COMPRESS","SAVE_RAND"};

	// This enumeration is used for the flags
	// instead of individual boolean values
	public enum Flags { _32BIT, _64BIT, _128BIT, DELETE_CRYPT, KEY_PHRASE, KEY_RAND, NO_COMPRESS, SAVE_RAND };

	public BitFlag() // default constuctor
	{
		flags = 0;
	}
        
        public BitFlag(int pFlags)
        {
            flags = pFlags;
        }

	public byte getFlags() // get all 8 flags as one byte
	{
		return (byte) flags;
	}

	public String getFlagsBinary()
	{
		return Integer.toBinaryString(flags);
	}

	// Method setAll( boolean ) use parameter true or false to set or clear all the flags at once
	public void setAll(boolean b)
	{
		for (int i=0; i < 8; i++)
		{
			set(i,b);
		}
	}

	public void setAll()
	{
		setAll(true);
	}

	public void clearAll()
	{
		setAll(false);
	}

	/*
	Method set( enum, boolean )

	call this from Main to set individual flags to true or false

	for example:
	BitFlag flag = new BitFlag();
	flag.set(Flags.RUNNING, true);
	*/
	public void set(Flags pFlags, boolean b)
	{
		int index = pFlags.ordinal(); // turn enum into corresponding int value
		set( index, b);
	}

	private void set(int index, boolean b)
	{
		if (b) // if true, set flag
		{
			setFlagAt(index);
		}
		else // if false, clear flag
		{
			clearFlagAt(index);
		}
	}

	public void set(Flags pFlags)
	{
		set(pFlags, true);
	}

	public void clear(Flags pFlags)
	{
		set(pFlags, false);
	}



	// returns true/false the value at bit(index)
	private boolean getFlagAt(int index)
	{
		int f = 1 << index;  // creates a variable with a '1' at just the bit index we want
											// using 2 to the power of index
		return ((flags & f) == f); // use binary AND to see if it is a 1
	}

	// public Method get( enum )
	// call from main to get individual flag values
	public boolean get(Flags pFlags)
	{
		int index = pFlags.ordinal();
		return getFlagAt(index);
	}

	// set flag to true at given index
	private void setFlagAt(int index)
	{
		int f = 1 << index;
		flags = flags | f; // use binary OR to set flag
	}

	// public Method toggle( enum ) to flip value of given flag
	public void toggle(Flags pFlags)
	{
		int index = pFlags.ordinal(); // convert to int
		toggleFlagAt(index); // send to private method
	}

	private void toggleFlagAt(int index)
	{
		int f = 1 << index; // use bit shifting instead of Math.pow
		flags = flags ^ f;  // use XOR exclusive OR to toggle the bit
	}

	private void clearFlagAt(int index)
	{
		int f = 1 << index;
		f = ~f & 0xFF; // use NOT and a MASK to get the opposite(complement) value
						// this returns an int with all 1's except for the index position
		flags = flags & f; // use mask against total using AND with a ZERO
	}

        @Override
	public String toString() // display all flag values
	{
		String retval = "";
                retval += "===================\n";
		//retval = "Byte Value:" + getFlags() + "\n";
		//retval +="Binary:" + getFlagsBinary() + "\n";
		for (int i = 0; i < 8; i++)
		{
			retval += "Flag:" + flagNameArray[i] + " = " + getFlagAt(i) + "\n";
		}
		return retval;
	}




}