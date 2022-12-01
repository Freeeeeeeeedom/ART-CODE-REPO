package dt.mutant.Encoder;
import dt.original.*;

// This is mutant program.
// Author : ysma

public class Encoder_LOR_28 extends Encoder
{

    public java.lang.String exe( byte[] octetString )
    {
        final char[] ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
        int bits24 = 0;
        int bits6 = 0;
        char[] out = new char[((octetString.length - 1) / 3 + 1) * 4];
        int outIndex = 0;
        int i = 0;
        while (i + 3 <= octetString.length) {
            bits24 = (octetString[i++] & 0xFF) << 16;
            bits24 |= (octetString[i++] & 0xFF) << 8;
            bits24 |= octetString[i++];
            bits6 = (bits24 & 0x00FC0000) >> 18;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & 0x0003F000) >> 12;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & 0x00000FC0) >> 6;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = bits24 & 0x0000003F;
            out[outIndex++] = ALPHABET[bits6];
        }
        if (octetString.length - i == 2) {
            bits24 = (octetString[i] & 0xFF) << 16;
            bits24 |= (octetString[i + 1] & 0xFF) << 8;
            bits6 = (bits24 & 0x00FC0000) >> 18;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & 0x0003F000) >> 12;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & 0x00000FC0) >> 6;
            out[outIndex++] = ALPHABET[bits6];
            out[outIndex++] = '=';
        } else {
            if (octetString.length - i == 1) {
                bits24 = (octetString[i] & 0xFF) << 16;
                bits6 = (bits24 & 0x00FC0000) >> 18;
                out[outIndex++] = ALPHABET[bits6];
                bits6 = (bits24 ^ 0x0003F000) >> 12;
                out[outIndex++] = ALPHABET[bits6];
                out[outIndex++] = '=';
                out[outIndex++] = '=';
            }
        }
        return new java.lang.String( out );
    }

}
