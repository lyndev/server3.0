/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.room.bean;

import java.util.Stack;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author undakunteera
 */
public class ZJHCardController
{
    private final static Logger logger     = Logger.getLogger(ZJHCardController.class);
    public final int    MAX_COUNT = 3;   // 最大数目
    public final int    DRAW      = 2;   // 和局类型
    
    public final byte   LOGIC_MASK_COLOR =(byte) 0xF0;   // 花色掩码
    public final byte   LOGIC_MASK_VALUE =(byte) 0x0F;   // 数值掩码
   
        //扑克类型
        public enum CardPatternType {
            CT_SINGLE("单牌类型", 1),
            CT_DOUBLE("对子类型", 2), 
            CT_SHUN_ZI("顺子类型", 3), 
            CT_JIN_HUA("金花类型", 4), 
            CT_SHUN_JIN("顺金类型", 5), 
            CT_BAO_ZI("豹子类型", 6),
            CT_SPECIAL("特殊类型", 7);

            private String name ;
            private int index ;

            private CardPatternType( String name , int index ){
                this.name = name ;
                this.index = index ;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }
    }

    //扑克
    public enum CardType {
        NONE("无效牌",       0x00, 0),
        FANG_KUAI_1("方块A",       0x01, 1),
        FANG_KUAI_2("方块2",       0x02, 2),
        FANG_KUAI_3("方块3",       0x03, 3),
        FANG_KUAI_4("方块4",       0x04, 4),
        FANG_KUAI_5("方块5",       0x05, 5),
        FANG_KUAI_6("方块6",       0x06, 6),
        FANG_KUAI_7("方块7",       0x07, 7),
        FANG_KUAI_8("方块8",       0x08, 8),
        FANG_KUAI_9("方块9",       0x09, 9),
        FANG_KUAI_10("方块10",   0x0A, 10),
        FANG_KUAI_J("方块J",     0x0B, 11), 
        FANG_KUAI_Q("方块Q",     0x0C, 12),
        FANG_KUAI_K("方块K",     0x0D, 13),

        MEI_HUA_1("梅花A",        0x11, 14),
        MEI_HUA_2("梅花2",        0x12, 15),
        MEI_HUA_3("梅花3",        0x13, 16),
        MEI_HUA_4("梅花4",        0x14, 17),
        MEI_HUA_5("梅花5",        0x15, 18),
        MEI_HUA_6("梅花6",        0x16, 19),
        MEI_HUA_7("梅花7",        0x17, 20),
        MEI_HUA_8("梅花8",        0x18, 21),
        MEI_HUA_9("梅花9",        0x19, 22),
        MEI_HUA_10("梅花10",    0x1A, 23),
        MEI_HUA_J("梅花J",      0x1B, 24), 
        MEI_HUA_Q("梅花Q",      0x1C, 25),
        MEI_HUA_K("梅花K",      0x1D, 26),

        HONG_TAO_1("红桃A",     0x21, 27),
        HONG_TAO_2("红桃2",     0x22, 28),
        HONG_TAO_3("红桃3",     0x23, 28),
        HONG_TAO_4("红桃4",     0x24, 30),
        HONG_TAO_5("红桃5",     0x25, 31),
        HONG_TAO_6("红桃6",     0x26, 32),
        HONG_TAO_7("红桃7",     0x27, 33),
        HONG_TAO_8("红桃8",     0x28, 34),
        HONG_TAO_9("红桃9",     0x29, 35),
        HONG_TAO_10("红桃10", 0x2A, 36),
        HONG_TAO_J("红桃J",   0x2B, 37), 
        HONG_TAO_Q("红桃Q",   0x2C, 38),
        HONG_TAO_K("红桃K",   0x2D, 39),

        HEI_TAO_1("黑桃A",     0x31, 40),
        HEI_TAO_2("黑桃2",     0x32, 41),
        HEI_TAO_3("黑桃3",     0x33, 42),
        HEI_TAO_4("黑桃4",     0x34, 43),
        HEI_TAO_5("黑桃5",     0x35, 44),
        HEI_TAO_6("黑桃6",     0x36, 45),
        HEI_TAO_7("黑桃7",     0x37, 46),
        HEI_TAO_8("黑桃8",     0x38, 47),
        HEI_TAO_9("黑桃9",     0x39, 48),
        HEI_TAO_10("黑桃10", 0x3A, 49),
        HEI_TAO_J("黑桃J",   0x3B, 50), 
        HEI_TAO_Q("黑桃Q",   0x3C, 51),
        HEI_TAO_K("黑桃K",   0x3D, 52);


        private String name;
        private int byteValue;
        private int index;

        private CardType( String name, int byteValue, int idnex){
            this.name = name ;
            this.byteValue = byteValue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCardByte() {
            return byteValue;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

    }
        
                   
    public CardType getCardTypeByValue(byte value){
        for (CardType type: CardType.values() ) {
            if(type.getCardByte() == value){
                return type;
            }
        }
        return CardType.NONE;
    }   
          
    //获取类型
    public CardPatternType GetCardPatternType(byte cbCardData[], int cbCardCount)
    {
        ////ASSERT(cbCardCount==MAX_COUNT);

        if (cbCardCount==MAX_COUNT)
        {
            //变量定义
            boolean cbSameColor=true, bLineCard=true;
            byte cbFirstColor = GetCardColor(cbCardData[0]);
            byte cbFirstValue = GetCardLogicValue(cbCardData[0]);

            //牌形分析
            for (byte i=1;i<cbCardCount;i++)
            {
                //数据分析
                if (GetCardColor(cbCardData[i])!=cbFirstColor) cbSameColor=false;
                if (cbFirstValue!=(GetCardLogicValue(cbCardData[i])+i)) bLineCard=false;

                //结束判断
                if ((cbSameColor==false)&&(bLineCard==false)) break;
            }

            //特殊A32
            if(!bLineCard)
            {
                boolean bOne=false,bTwo=false,bThree=false;
                for(byte i=0;i<MAX_COUNT;i++)
                {
                    switch (GetCardValue(cbCardData[i]))
                    {
                        case 1:
                            bOne=true;
                            break;
                        case 2:
                            bTwo=true;
                            break;
                        case 3:
                            bThree=true;
                            break;
                        default:
                            break;
                    }
                }
                if(bOne && bTwo && bThree)bLineCard=true;
            }

            //顺金类型
            if ((cbSameColor)&&(bLineCard)) return CardPatternType.CT_SHUN_JIN;

            //顺子类型
            if ((!cbSameColor)&&(bLineCard)) return CardPatternType.CT_SHUN_ZI;

            //金花类型
            if((cbSameColor)&&(!bLineCard)) return CardPatternType.CT_JIN_HUA;

            //牌形分析
            boolean bDouble=false,bPanther=true;

            //对牌分析
            for (int i=0;i<cbCardCount-1;i++)
            {
                for (int j=i+1;j<cbCardCount;j++)
                {
                    if (GetCardLogicValue(cbCardData[i])==GetCardLogicValue(cbCardData[j])) 
                    {
                        bDouble=true;
                        break;
                    }
                }
                if(bDouble)break;
            }

            //三条(豹子)分析
            for (byte i=1;i<cbCardCount;i++)
            {
                if (bPanther && cbFirstValue!=GetCardLogicValue(cbCardData[i])) bPanther=false;
            }

            //对子和豹子判断
            if (bDouble==true){
                return (bPanther)?CardPatternType.CT_BAO_ZI:CardPatternType.CT_DOUBLE; 
            }
               

            //特殊235
            boolean bTwo=false,bThree=false,bFive=false;
            for (byte i=0;i<cbCardCount;i++)
            {
                switch (GetCardValue(cbCardData[i]))
                {
                    case 2:          
                        bTwo=true;
                        break;
                    case 3:
                        bThree=true;
                        break;
                    case 5:
                        bFive=true;
                        break;
                    default:
                        break;
                }
            }   
            if (bTwo && bThree && bFive) return CardPatternType.CT_SPECIAL;
        }

        return CardPatternType.CT_SINGLE;
    }
    
    //获取数值
    public byte GetCardValue(byte cbCardData) { return (byte) (cbCardData&LOGIC_MASK_VALUE); }

    //获取花色
    public byte GetCardColor(byte cbCardData) { return (byte) (cbCardData&LOGIC_MASK_COLOR); }


    //排列扑克
    public byte SortCardList(byte cbCardData[], int cbCardCount){
        //转换数值
        byte[] cbLogicValue = new byte[MAX_COUNT];
        for (byte i=0;i<cbCardCount;i++) cbLogicValue[i]=GetCardLogicValue(cbCardData[i]);  

        //排序操作
        boolean bSorted=true;
        byte cbTempData, bLast= (byte) (cbCardCount-1);
        do
        {
            bSorted=true;
            for (byte i=0;i<bLast;i++)
            {
                if ((cbLogicValue[i]<cbLogicValue[i+1])||
                    ((cbLogicValue[i]==cbLogicValue[i+1])&&(cbCardData[i]<cbCardData[i+1])))
                {
                    //交换位置
                    cbTempData=cbCardData[i];
                    cbCardData[i]=cbCardData[i+1];
                    cbCardData[i+1]=cbTempData;
                    cbTempData=cbLogicValue[i];
                    cbLogicValue[i]=cbLogicValue[i+1];
                    cbLogicValue[i+1]=cbTempData;
                    bSorted=false;
                }   
            }
            bLast--;
        } while(bSorted==false);

        return 0;
    }
        
    //发牌到玩家手中
    public void RandCardList(Stack<Byte> cbCardBuffer, byte[][] players, int cbBufferCount){
        for (byte[] player : players)
        {
            for(int j = 0; j < cbBufferCount; j++){
                byte _card = cbCardBuffer.pop();
                player[j] = _card;
            }
        }
    }
    
    // 洗牌
    public Stack<Byte> shuffle(byte[] arr){
        byte[] newArr = arr.clone();
        for (int i = newArr.length - 1; i >= 0; i--) {
            // 随机范围[0,1)
            int randomIndex = (int) Math.floor(RandomUtils.nextInt(100) * 0.01 * (i + 1));
            byte itemAtIndex = newArr[randomIndex];
            newArr[randomIndex] = newArr[i];
            newArr[i] = itemAtIndex;
        }
        
        Stack<Byte> cardStack = new Stack<>();
        for(int i = 0; i < newArr.length; i++){
            cardStack.push(newArr[i]);
        }
        return cardStack;
    }
    
    //逻辑数值
    public byte GetCardLogicValue(byte cbCardData){
        //扑克属性
        byte bCardColor=GetCardColor(cbCardData);
        byte bCardValue=GetCardValue(cbCardData);

        //转换数值
        return (byte) ((bCardValue==1)?(bCardValue+13):bCardValue);
    }
    
    //对比扑克 : 
    // 0 = 前者比后者小
    // 1 = 前者比后者大
    // 2 = 前者和后者一样大
    public byte CompareCard(byte cbFirstData[], byte cbNextData[])
    {
        //设置变量
        byte[] FirstData= new byte [MAX_COUNT];
        byte[] NextData = new byte [MAX_COUNT];
        FirstData = cbFirstData.clone();
        NextData = cbNextData.clone();

        //大小排序
        SortCardList(FirstData,MAX_COUNT);
        SortCardList(NextData,MAX_COUNT);

        //获取类型
        CardPatternType cbNextType = GetCardPatternType(NextData,MAX_COUNT);
        CardPatternType cbFirstType=GetCardPatternType(FirstData,MAX_COUNT);

        //特殊情况分析
        if((cbNextType.getIndex()+cbFirstType.getIndex())==(CardPatternType.CT_SPECIAL.getIndex()+CardPatternType.CT_BAO_ZI.getIndex())){
            if(cbFirstType.getIndex()>cbNextType.getIndex()){
                return 1;
            } else {
                return 0;
            }
        }
            
        //还原单牌类型
        if(cbNextType==CardPatternType.CT_SPECIAL)cbNextType=CardPatternType.CT_SINGLE;
        if(cbFirstType==CardPatternType.CT_SPECIAL)cbFirstType=CardPatternType.CT_SINGLE;

        //类型判断
        if (cbFirstType.getIndex()!=cbNextType.getIndex()) return (byte) ((cbFirstType.getIndex()>cbNextType.getIndex())?1:0);

        //简单类型
        switch(cbFirstType)
        {
            case CT_BAO_ZI:         //豹子
            case CT_SINGLE:         //单牌
            case CT_JIN_HUA:        //金花
                {
                    //对比数值
                    for (int i=0;i<MAX_COUNT;i++)
                    {
                        byte cbNextValue=GetCardLogicValue(NextData[i]);
                        byte cbFirstValue=GetCardLogicValue(FirstData[i]);
                        if (cbFirstValue!=cbNextValue) return (byte) ((cbFirstValue>cbNextValue)?1:0);
                    }
                    return DRAW;
                }
            case CT_SHUN_ZI:        //顺子
            case CT_SHUN_JIN:       //顺金 432>A32
                {       
                    byte cbNextValue=GetCardLogicValue(NextData[0]);
                    byte cbFirstValue=GetCardLogicValue(FirstData[0]);

                    //特殊A32
                    if(cbNextValue==14 && GetCardLogicValue(NextData[MAX_COUNT-1])==2)
                    {
                        cbNextValue=3;
                    }
                    if(cbFirstValue==14 && GetCardLogicValue(FirstData[MAX_COUNT-1])==2)
                    {
                        cbFirstValue=3;
                    }

                    //对比数值
                    if (cbFirstValue!=cbNextValue) return (byte) ((cbFirstValue>cbNextValue)?1:0);;
                    return DRAW;
                }
            case CT_DOUBLE:         //对子
                {
                    byte cbNextValue=GetCardLogicValue(NextData[0]);
                    byte cbFirstValue=GetCardLogicValue(FirstData[0]);

                    //查找对子/单牌`
                    byte bNextDouble=0,bNextSingle=0;
                    byte bFirstDouble=0,bFirstSingle=0;
                    if(cbNextValue==GetCardLogicValue(NextData[1]))
                    {
                        bNextDouble=cbNextValue;
                        bNextSingle=GetCardLogicValue(NextData[MAX_COUNT-1]);
                    }
                    else
                    {
                        bNextDouble=GetCardLogicValue(NextData[MAX_COUNT-1]);
                        bNextSingle=cbNextValue;
                    }
                    if(cbFirstValue==GetCardLogicValue(FirstData[1]))
                    {
                        bFirstDouble=cbFirstValue;
                        bFirstSingle=GetCardLogicValue(FirstData[MAX_COUNT-1]);
                    }
                    else 
                    {
                        bFirstDouble=GetCardLogicValue(FirstData[MAX_COUNT-1]);
                        bFirstSingle=cbFirstValue;
                    }

                    if (bNextDouble!=bFirstDouble)return (byte) ((bFirstDouble>bNextDouble)?1:0);
                    if (bNextSingle!=bFirstSingle)return (byte) ((bFirstSingle>bNextSingle)?1:0);
                    return DRAW;
                }
            }
        return DRAW;
    }
      
}
