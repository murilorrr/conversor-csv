package com.trybe.conversorcsv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Conversor {

  /**
   * Função utilizada apenas para validação da solução do desafio.
   *
   * @param args Não utilizado.
   * @throws IOException Caso ocorra algum problema ao ler os arquivos de entrada ou
   *                     gravar os arquivos de saída.
   */
  public static void main(String[] args) throws IOException {
    File pastaDeEntradas = new File("./entradas/");
    File pastaDeSaidas = new File("./saidas/");
    new Conversor().converterPasta(pastaDeEntradas, pastaDeSaidas);
  }

  /**
   * Converte todos os arquivos CSV da pasta de entradas. Os resultados são gerados
   * na pasta de saídas, deixando os arquivos originais inalterados.
   *
   * @param pastaDeEntradas Pasta contendo os arquivos CSV gerados pela página web.
   * @param pastaDeSaidas Pasta em que serão colocados os arquivos gerados no formato
   *                      requerido pelo subsistema.
   *
   * @throws IOException Caso ocorra algum problema ao ler os arquivos de entrada ou
   *                     gravar os arquivos de saída.
   */
  public void converterPasta(File pastaDeEntradas, File pastaDeSaidas) throws IOException {
    if (!pastaDeSaidas.exists()) {
      pastaDeSaidas.mkdir();
    }

    for (File arquivo: pastaDeEntradas.listFiles()) {
      if (arquivo.getName().endsWith(".csv")) {
        FileReader reader = new FileReader(arquivo);
        BufferedReader buffedFile = new BufferedReader(reader);
        String conteudoLinha = buffedFile.readLine();
        ArrayList<String[]> lista = new ArrayList<String[]>();
        String regex = ",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))";
        Pattern p = Pattern.compile(regex);

        while(conteudoLinha != null) {
          String[] split = p.split(conteudoLinha);
          lista.add(split);
          conteudoLinha = buffedFile.readLine();
        }
        fecharObjetosLeitura(reader, buffedFile);
        
        File saida = new File(pastaDeSaidas, arquivo.getName());
        FileWriter writer = new FileWriter(saida);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        
        for (String[] split : lista) {
          if (lista.indexOf(split) != 0) {
            String nome = split[0].toUpperCase();
            // 29/07/1962
            String data = split[1].substring(6, 10)
                + '-' + split[1].substring(3, 5)
                + '-' + split[1].substring(0, 2);
            String email = split[2].toLowerCase();
            // 123.456.789-09.
            // 77734125700
            String cpf = split[3].substring(0, 3)
                + '.' + split[3].substring(3, 6)
                + '.' + split[3].substring(6, 9)
                + '-' + split[3].substring(9, 11);
            String linha = nome + "," + data + "," + email + "," + cpf;
            bufferedWriter.write(linha);
            bufferedWriter.newLine();
          }
          else {
            for (String item: split) {
              if (item == split[3]) {
                bufferedWriter.write(item);
                bufferedWriter.newLine();
              } else {
                bufferedWriter.write(item + ',');
              }
            }
          }
        }
        bufferedWriter.flush(); // grava o arquivo
        fecharObjetosEscrita(writer, bufferedWriter);
      }
    }
    System.out.println("acabou");

  }

  private void fecharObjetosLeitura(FileReader fileReader, BufferedReader bufferedReader) { 
    try { 
      fileReader.close();
      bufferedReader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void fecharObjetosEscrita(FileWriter fileWriter, BufferedWriter bufferedWriter) { 
    try { 
      fileWriter.close();
      bufferedWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

// regex reference https://stackoverflow.com/questions/18144431/regex-to-split-a-csv