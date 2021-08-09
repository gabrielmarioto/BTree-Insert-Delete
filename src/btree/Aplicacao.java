/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btree;

/**
 *
 * @author Gabriel
 */
public class Aplicacao
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here
        BTree arvoreB = new BTree();

        for (int i = 1; i <= 1000; i++)        
            arvoreB.inserir(i, i);
        
        for (int i = 800; i < 1000; i++)        
            arvoreB.excluir(i);
 
        arvoreB.inOrdem();
    }

}
