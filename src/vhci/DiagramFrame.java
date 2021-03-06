/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Phr34z3r
 */
public class DiagramFrame extends javax.swing.JFrame {

    int[] data = {
        21, 14, 18, 03, 86, 88, 74, 87, 54, 77,
        61, 55, 48, 60, 49, 36, 38, 27, 20, 18
    };
    int PAD = 20;
    
    /**
     * Creates new form DiagramFrame
     */
    public DiagramFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 812, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public int[] getData() {
        return data;
    }

    public int getPAD() {
        return PAD;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public void setPAD(int PAD) {
        this.PAD = PAD;
    }

    public void updateChart(int type, DefaultCategoryDataset data, int longt, int lat){
        JFreeChart Chart = null;
       
        
        
        
        switch( type ){
            case 0:
                Chart = ChartFactory.createLineChart( "Longtitude: "+longt+" Latitude: "+lat,
                                                      "Date","cm",
                                                      data,
                                                      PlotOrientation.HORIZONTAL,
                                                      true,true,false);
                
                break;
            case 1:
                Chart = ChartFactory.createBarChart( "Longtitude: "+longt+" Latitude: "+lat,
                                                      "Date","cm",
                                                      data,
                                                      PlotOrientation.HORIZONTAL,
                                                      true,true,false);
                break;
            case 2:
                Chart = ChartFactory.createStackedBarChart("Earth Mass Change",
                                                      "Date","Longtitude: "+longt+" Latitude: "+lat,
                                                      data,
                                                      PlotOrientation.HORIZONTAL,
                                                      true,true,false);
                        
                break;
            
        }
        
       
         
        ChartPanel chartPanel = new ChartPanel( Chart );

        chartPanel.setMaximumDrawWidth(800);  
        chartPanel.setMaximumDrawHeight(2500);

        chartPanel.setMinimumDrawWidth(800);
        chartPanel.setMinimumDrawHeight(2500);
        chartPanel.setPreferredSize(new Dimension(800, 2500));


        this.setContentPane(new JScrollPane(chartPanel) );
      
   }
    
   private void updateLineChart(){
       
   }

   private DefaultCategoryDataset createDataset( ) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      
      dataset.addValue( -300 , "schools" , "1910" );
      dataset.addValue( -240 , "schools" , "1920" );
      dataset.addValue( -120 , "schools" , "1930" );
      dataset.addValue( -60 , "schools" ,  "1940" );
      dataset.addValue( -30 , "schools" , "1950" );
      dataset.addValue( -15 , "schools" , "1960" );
      
      
      
      dataset.addValue( 15 , "schools" , "1970" );
      dataset.addValue( 30 , "schools" , "1980" );
      dataset.addValue( 60 , "schools" ,  "1990" );
      dataset.addValue( 120 , "schools" , "2000" );
      dataset.addValue( 240 , "schools" , "2010" );
      dataset.addValue( 300 , "schools" , "2014" );
      return dataset;
   }
    
}
