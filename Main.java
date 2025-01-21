import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class JListCustomRenderer extends JFrame {

    public static List<String> duplicates = new ArrayList<>();
    static ArrayList<Integer> nColor = new ArrayList<>();//Индекс определяющий цвет строки в списке

    class Dublicat_Render extends JLabel implements ListCellRenderer<String> {

    public void Dublicat_Renderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String dubl, int index, boolean isSelected, boolean cellHasFocus) {

            setText(dubl);

            if (nColor.get(index) == 0) {
                //setBackground(Color.WHITE);
                setForeground(Color.RED);
            } else {
                //setBackground(Color.BLUE);
                setForeground(Color.ORANGE);
            }
            return this;
        }
    }

    //create the model and add elements
    DefaultListModel<String> listModel = new DefaultListModel<>();

    public static ArrayList<String> myFiles = new ArrayList<String>();

    public JListCustomRenderer() {

        //create the list
        JList<String> pathList = new JList<>(listModel);

        //create the button
        JButton button = new JButton("Выбрать папку");
        button.setSize(10, 20);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(button);
                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();

                    String root_dir = file.getAbsolutePath();
                    Path rootPath = Paths.get(root_dir);//имя папки по выбранному пути

                    List<Path> allFiles = new ArrayList<>();
                    ListAllFiles all_Files = new ListAllFiles();
                    try {
                        all_Files.listAllFiles(rootPath, allFiles);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    for (Path k : allFiles) {
                        String name = String.valueOf(k.getFileName());

                        if (name.contains("mp3")) {
                            myFiles.add(name);
                        }
                    }

                    //Поиск дубликатов в названиях файлов
                    Set<String> set = new HashSet<>();
                    myFiles.forEach(n -> {
                        if (!set.add(n)) {
                            duplicates.add(n);
                        }
                    });

                    int n = 0;//Номер дубликата файла
                    for (String i : duplicates) {
                        int m = 0;
                        nColor.add(m);
                        listModel.addElement(duplicates.get(n));
                        for (Path k : allFiles) {
                            if (k.toString().contains(i)) {
                                m = 1;
                                nColor.add(m);
                                listModel.addElement(k.toString());
                            }
                        }
                        n = n + 1;
                        m = 0;
                    }
                    System.out.println("Индексы цвета" + nColor);

                    pathList.addListSelectionListener(new ListSelectionListener() {

                        @Override
                        public void valueChanged(ListSelectionEvent arg0) {
                            if (!arg0.getValueIsAdjusting()) {
                                System.out.println(pathList.getSelectedValue().toString());

                                String path_file = pathList.getSelectedValue().toString();
                                int index = path_file.lastIndexOf("/");
                                System.out.println("last index / = " + index);

                                System.out.println(path_file.substring(0, index));
                                String path_dir = path_file.substring(0, index);

                                Desktop desktop = Desktop.getDesktop();
                                File dirToOpen = null;
                                try {
                                    dirToOpen = new File(path_dir);
                                    try {
                                        desktop.open(dirToOpen);
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                } catch (IllegalArgumentException iae) {
                                    System.out.println("File Not Found");
                                }

                            }
                        }
                    });

                }
            }
        });

        JPanel panel = new JPanel();
        panel.setMinimumSize(new Dimension(350, 250));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 5, 10, 5);

        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(button, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add((new JScrollPane(pathList)), constraints);

        pathList.setMinimumSize(new Dimension(250, 150));
        pathList.setCellRenderer(new Dublicat_Render());

        String value = pathList.getSelectedValue();
        System.out.println(value);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Поиск дубликатов mp3 файлов");
        this.setSize(500, 300);
        this.setContentPane(panel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JListCustomRenderer();
            }
        });
    }
}