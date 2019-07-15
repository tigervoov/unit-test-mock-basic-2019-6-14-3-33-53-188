package cashregister;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

public class CashRegisterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private final PrintStream originOut =System.out;

    @BeforeEach
    public void setUpStreams(){
        System.setOut(new PrintStream(outContent));
    }
    @AfterEach
    public void restoreStreams(){
        System.setOut(originOut);
    }
    @Test
    public void should_print_the_real_purchase_when_call_process() {
        //given
        Purchase purchase=new Purchase(new Item[]{
                new Item("apple",3.12),
                new Item("banana",2.11),
        });
        Printer printer=new Printer();
        CashRegister cashRegister=new CashRegister(printer);
        //when
        cashRegister.process(purchase);
        //then
        Assertions.assertEquals("apple\t3.12\nbanana\t2.11\n",outContent.toString());
    }

    @Test
    public void should_print_the_stub_purchase_when_call_process() {
        //given
        Purchase purchase=new Purchase(new Item[]{
                new Item("apple",3.12),
                new Item("banana",2.11),
        });
        CashRegister cashRegister=new CashRegister(new Printer(){
            @Override
            public void print(String printThis) {
                super.print("sub:"+printThis);
            }
        });
        //when
        cashRegister.process(purchase);
        //then
        Assertions.assertEquals("sub:apple\t3.12\nbanana\t2.11\n",outContent.toString());
    }

    @Test
    public void should_verify_with_process_call_with_mockito() {
        //given
        Purchase purchase=new Purchase(new Item[]{
                new Item("apple",3.12),
                new Item("banana",2.11),
        });
        Printer printer=mock(Printer.class);
        CashRegister cashRegister=new CashRegister(printer);

        //when
        cashRegister.process(purchase);

        //then
        ArgumentCaptor<String> argumentCaptor=ArgumentCaptor.forClass(String.class);
        verify(printer).print(argumentCaptor.capture());
        Assertions.assertEquals("apple\t3.12\nbanana\t2.11\n",argumentCaptor.getValue());
    }

}
