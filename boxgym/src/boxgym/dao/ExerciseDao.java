package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Exercise;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class ExerciseDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public ExerciseDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(Exercise exercise) {
        String sql = "INSERT INTO `exercise` (`name`, `exerciseType`, `exerciseGroup`, `description`, `instruction`) "
                + "VALUES (?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, exercise.getName());
            ps.setString(2, exercise.getExerciseType());
            ps.setString(3, exercise.getExerciseGroup());
            ps.setString(4, exercise.getDescription());
            ps.setString(5, exercise.getInstruction());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public LinkedHashMap<Integer, String> getExerciseForHashMap() {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT `exerciseId`, `name` FROM `exercise`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Exercise e;
            while (rs.next()) {
                e = new Exercise(rs.getInt("exerciseId"), rs.getString("name"));
                map.put(e.getExerciseId(), e.getName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return map;
    }

    public List<Exercise> read() {
        List<Exercise> exercisesList = new ArrayList<>();
        String sql = "SELECT * FROM `exercise`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Exercise e = new Exercise();
                e.setExerciseId(rs.getInt("exerciseId"));
                e.setName(rs.getString("name"));
                e.setExerciseType(rs.getString("exerciseType"));
                e.setExerciseGroup(rs.getString("exerciseGroup"));
                e.setDescription(rs.getString("description"));
                e.setInstruction(rs.getString("instruction"));
                e.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                e.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                exercisesList.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return exercisesList;
    }

    public boolean update(Exercise exercise) {
        String sql = "UPDATE `exercise` SET `name` = ?, `exerciseType` = ?, `exerciseGroup` = ?, `description` = ?, `instruction` = ? "
                + "WHERE `exerciseId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, exercise.getName());
            ps.setString(2, exercise.getExerciseType());
            ps.setString(3, exercise.getExerciseGroup());
            ps.setString(4, exercise.getDescription());
            ps.setString(5, exercise.getInstruction());
            ps.setInt(6, exercise.getExerciseId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean delete(Exercise exercise) {
        String sql = "DELETE FROM `exercise` WHERE `exerciseId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, exercise.getExerciseId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean checkWorkoutDeleteConstraint(int exerciseId) {
        String sql = "SELECT `fkExercise` FROM `workout_exercise` WHERE `fkExercise` = " + exerciseId + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<String> filterExercisesByGroup(String exerciseGroup) {
        List<String> exercisesList = new ArrayList<>();
        String sql = "SELECT `name` FROM `exercise` WHERE `exerciseGroup` = '" + exerciseGroup + "';";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                exercisesList.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return exercisesList;
    }

    public boolean importExercises() {
        String sql = "INSERT INTO `exercise` (`name`, `exerciseType`, `exerciseGroup`, `description`, `instruction`) VALUES\n"
                + "	('Abdominal Amplitude Máxima', 'Funcional', 'Abdômen', 'Exercício com foco no fortalecimento e hipertrofia da região abdominal.', '1. Deite sobre um colchonete; 2. Manter os joelhos flexionados, pés ligeiramente separados; 3. Coloque as mãos entrelaçadas atrás do pescoço; 4. Inspire e eleve os ombros em direção ao joelho; 5. Expire lentamente e retorne à posição inicial.'),\n"
                + "	('Abdominal Bicicleta', 'Funcional', 'Abdômen', 'O exercício trabalha os músculos abdominais com ênfase na parte inferior, estimula também, o equilíbrio e coordenação motora. Auxilia no desenvolvimento de um abdômen mais definido e proporciona variedade ao treino.', '1. Deite sobre um colchonete; 2. Eleve as pernas do solo formando um ângulo de 90 graus; 3. Mãos entrelaçadas atrás do pescoço, contraia o abdômen e estenda a perna direita e flexione a esquerda; 4. Simultaneamente, alterne a posição das pernas; 5. Realize os movimentos concentrando a força nos músculos abdominais, conforme o número de repetições orientado pelo professor.'),\n"
                + "	('Abdominal Canivete', 'Funcional', 'Abdômen', 'Exercício para fortalecimento e hipertrofia da região abdominal, reto abdominal, oblíquos internos e externos. Indicado a praticante de musculação nível iniciante e intermediário. Estimula o equilíbrio, coordenação, força muscular e capacidade funcional.', '1. Deite sobre um colchonete; 2. Joelhos flexionados e pernas entrelaçadas; 3. Mãos entrelaçadas atrás do pescoço, manter o queixo alinhado ao peitoral; 4. Realize a flexão do tronco elevando os deltoides; 5. Simultaneamente, realize a elevação dos membros inferiores, em seguida, retorne à posição inicial.'),\n"
                + "	('Abdominal Canoa', 'Funcional', 'Abdômen', 'É um exercício de isometria que trabalha os músculos da região do abdômen, principalmente a coordenação e resistência do core.', '1. Deitado, braços estendidos atrás da cabeça, pernas estendidas, pés unidos; 2. Com o abdômen bem contraído, dê um impulso com o tronco, elevando as pernas e os braços (o corpo deve ficar no formato de uma canoa); 3. Mantenha a posição pelo tempo predefinido pelo professor.'),\n"
                + "	('Abdominal Giro Russo', 'Funcional', 'Abdômen', 'O exercício é ótimo para fortalecer os músculos oblíquos e da lombar, além dos músculos abdominais.', '1. Levante o tronco de modo que você fique quase sentado; 2. Levante as pernas para que fiquem suspensas sem tocar no tapete, mantendo a sola dos pés apoiada; 3. Em seguida, estique os braços completamente e entrelace os dedos; 4. Você deve equilibrar a parte superior de seu tronco, sempre ficando em linha reta, de um lado primeiro, até chegar ao limite. Mantenha por dois segundos; 5. Em seguida, vire para o outro lado e repita a ação. O movimento completo para ambos os lados conta como uma repetição;'),\n"
                + "	('Extensão de Punho', 'Musculação', 'Antebraço', 'Exercício para fortalecimento e hipertrofia dos músculos dos antebraços, com enfoque aos músculos extensores do punho.', '1. Sente-se em um banco e segure a barra com uma pegada pronada; 2. Posicione os cotovelos sobre as coxas; 3. Mantendo os braços parados, flexione os pulsos para cima, levantando os halteres; 4. Lentamente, leve os pesos de volta à posição inicial.'),\n"
                + "	('Rosca Inversa no Cross', 'Musculação', 'Antebraço', 'Exercício para fortalecimento e hipertrofia dos músculos dos antebraços e bíceps.', '1. Ajuste a polia na posição mais baixa possível e prenda a barra reta; 2. Fique de frente para os pesos e segure a barra usando uma pegada pronada; 3. Mantenha as costas retas, o abdômen contraído; 4. Dê um passo para trás e deixe seus braços esticados; 5. Inicie o exercício dobrando o cotovelo e leve as mãos na direção dos ombros; 6. Estenda o cotovelo lentamente, de modo a voltar a posição inicial, completando uma repetição.'),\n"
                + "	('Rosca Alternada', 'Musculação', 'Bíceps', 'Exercício para fortalecimento e hipertrofia dos bíceps, com enfoque aos músculos bíceps braquiais.', '1. Utilize dois dumbbells ou halteres; 2. Posição em pé, cabeça e costas alinhadas; 3. Joelhos semiflexionados, pés ligeiramente afastados para obter uma base estável na execução do exercício; 4. Braços estendidos e, com os antebraços posicionados em supinação, realize a flexão unilateral do cotovelo levando o punho em direção ao ombro; 5. Retorne à posição inicial de forma controlada; 6. Realize novamente com o membro contralateral, alterne conforme o número de repetições orientado pelo professor(a).'),\n"
                + "	('Rosca Concentrada', 'Musculação', 'Bíceps', 'Exercício para fortalecimento e hipertrofia dos bíceps, com enfoque aos músculos bíceps braquiais. Realiza de forma unilateral, proporciona maior controle durante a execução.', '1. Utilize um dumbbells ou halter; 2. Sente na extremidade de um banco, pernas afastadas, pegue um halter com uma das mãos; 3. Joelhos flexionados, pés firmes ao solo, apoie o cotovelo na parte interna da coxa próximo ao joelho; 4. Segure o peso mantendo o cotovelo estendido, a pegada deverá ser neutra; 5. Apoie a mão que está livre sobre a outra perna para auxiliar na estabilização; 6. Cabeça e costas alinhadas e com esforço dos músculos do bíceps, suba o peso flexionando o cotovelo; 7. Retorne à posição inicial de forma controlada, repita os movimentos.'),\n"
                + "	('Rosca Martelo Inclinado', 'Musculação', 'Bíceps', 'Exercício para fortalecimento muscular e hipertrofia da região do bíceps. Tonificando e aumentando a circunferência dos braços. Ele possuí uma dificuldade de nível intermediário.', '1. Pegue um par de halteres e sente-se em um banco inclinado em 45 graus; 2. Mova as escápulas para trás e estenda totalmente os braços para baixo, permitindo que os halteres fiquem suspensos nas laterais do corpo com as palmas voltadas uma para a outra; 3. Flexione os cotovelos trazendo os halteres na direção de seus ombros; 4. Levante-os o mais alto possível enquanto mantém uma tensão constante no bíceps; 5. Pause, depois lentamente baixe os halteres de volta à posição inicial.'),\n"
                + "	('Agachamento com Elevação Frontal', 'Funcional', 'Corpo', 'O exercício tem como objetivo o fortalecimento e hipertrofia dos músculos da perna e ombros.', '1. Segure dois halteres com as palmas das mãos voltadas para o corpo; 2. Posicione os pesos à frente, cotovelo levemente flexionados; 3.  Manter o troco reto, escápulas encaixadas para trás e abdômen contraído; 4. Cotovelos semiflexionados, levante os dois halteres simultaneamente até a altura em que o braço fique em paralelo ao solo; 5. Retorne à posição inicial, com a coluna reta e abdômen contraído, desça o corpo flexionando os joelhos até que a coxa fique em um ângulo de 90 graus, em paralelo ao solo; 6. Os joelhos não deveram ultrapassar a linha dos pés; 7. Com a força das coxas, empurre o corpo para cima retornando à posição inicial e repita os movimentos.'),\n"
                + "	('Corda Naval Serpente', 'Crossfit', 'Corpo', 'O exercício trabalha a estabilidade e a produção de força do core, como também a resistência dos membros superiores.', '1. Segure as extremidades das cordas; 2. Posição anteroposterior, com leve flexão de joelhos, quadris e tronco e com os músculos do core sempre ativados; 3. Mova os braços de um lado para o outro e gire o tronco de forma simultânea, dessa forma a corda irá se mover como uma serpente; 4. Repita os movimentos pela quantidade de tempo orientada pelo professor(a).'),\n"
                + "	('Extensão Lombar', 'Musculação', 'Costas', 'Esse exercício trabalha os músculos da região lombar para ter um core fortalecido.  Uma das vantagens desse exercício é a de evitar possíveis lesões nos músculos e na coluna.', '1. Prepare uma cadeira romana; 2. Certifique-se de que a almofada da coxa esteja um pouco abaixo da cintura e de que os tornozelos fiquem encaixados sob o apoio de perna; 3. Cruze os braços e se incline para a frente e para baixo; 4. Mantenha o tronco firme e encaixado e o eleve até formar uma linha reta entre a cabeça e os tornozelos; 5. Faça uma pausa e abaixe o corpo lentamente de volta à posição inicial.'),\n"
                + "	('Puxada na Barra Fixa', 'Musculação', 'Costas', 'Exercício para fortalecimento e hipertrofia da região das dorsais, abrange também, músculos auxiliares, tais como, trapézio e bíceps braquial. O praticante deve elevar o próprio peso, com necessidade de um preparo físico e potência muscular. Indicado a praticante de musculação nível avançado.', '1. Segure uma barra fixa com as palmas das mãos voltadas para frente em uma distância maior do que a dos ombros; 2. Com as pernas suspensas, cruze os pés para apoio a região abdominal estabilizada; 3. Cotovelos estendidos e tronco ligeiramente inclinado para trás, manter a inclinação do corpo; 4. Eleve o peito em direção a barra com os cotovelos para trás, continue o movimento até a cabeça ultrapassar a linha das mãos; 5. Desça o corpo estendendo os cotovelos novamente, repita o movimento.'),\n"
                + "	('Remada Alta Hammer', 'Musculação', 'Costas', 'O exercício irá trabalhar o fortalecimento das costas, auxiliando também na melhora da postura.', '1. Sente-se no aparelho com os pés fixos; 2. Ajuste o apoio do peito de forma que seu braço fique estendido; 3. Mantenha a coluna reta e a cabeça para frente; 4. Segure as manoplas e puxe ao mesmo tempo em que contrai os músculos das costas; 5. Une as escápulas ao final do movimento e deixe o abdômen contraído o tempo todo para estabilizar a coluna; 6. Retorne os braços à posição inicial em um movimento contínuo, até que os cotovelos fiquem quase estendidos; 7. Repita os movimentos pela quantidade de vezes determinada pelo professor(a).'),\n"
                + "	('Remada na Polia', 'Musculação', 'Costas', 'Exercício para hipertrofia e fortalecimento as dorsais, semelhante à remada curvada e unilateral.  Os músculos das dorsais são recrutados em maior escala e inicialmente necessita de pouca técnica. Indicado a praticante nível iniciante e intermediário.', '1. Sente no banco do aparelho; 2. Pegue a barra com pegada em pronação, as mãos na mesma distancia a dos ombros; 3. Joelhos semiflexionados e pés apoiados na plataforma; 4. Com os cotovelos estendidos, puxe a barra até a região da cintura realizando a flexão dos cotovelos para trás do corpo; 5. Retorne a barra à posição inicial; 6. Repita o movimento.'),\n"
                + "	('Abdução de Perna com Faixa Elástica', 'Funcional', 'Glúteo', 'O exercício trabalha o fortalecimento da região dos glúteos. Como também a estabilização e flexibilidade do corpo.', '1. Apoie o corpo sobre os joelhos e os punhos; 2. Posicione a faixa de resistência  acima dos joelhos. Pescoço, costas e quadris devem estar alinhados; 3. Mova a perna esquerda para fora, esticando a faixa. O resto do corpo fica em parado: não gire para o lado; 4. Retorne à posição inicial. Faça todas as repetições de um lado e depois troque.'),\n"
                + "	('Agachamento com Passada Lateral', 'Musculação', 'Glúteo', 'Esse exercício trabalha a musculatura da região do glúteo e quadríceps, tonificando os glúteos e melhorando a mobilização da articulação do quadril.', '1. Manter os pés ligeiramente afastados e sempre bem apoiados no chão; 2. Esticar os braços à frente do corpo; 3. Manter as costas retas e evitar compensar com o quadril, como é comum acontecer; 4. Inspirar antes de iniciar o agachamento e soltar o ar enquanto desce levando uma perna para a direita. Descer o suficiente para manter as coxas paralelas ao chão; 5. Volte à posição inicial e repita o movimento agora levando a outra perna para esquerda.'),\n"
                + "	('Elevação Pélvica', 'Musculação', 'Glúteo', 'Exercício para hipertrofia e fortalecimento dos glúteos e dos músculos do abdômen e vasto medial. Melhora a estabilidade de quadril, tronco e mobilidade da coluna vertebral.', '1. Deite sobre um colchonete; 2. De barriga para cima com os braços estendidos a lado do corpo, posicione os joelhos flexionados e pés fixos ao solo; 3. Empurre o quadril para cima, estendendo o máximo que conseguir, contraindo a musculatura dos glúteos na execução; 4. Desça o quadril lentamente até ficar próximo do solo; 5. Elevar novamente o quadril; 6. Repita os movimentos, conforme o número de repetições orientado pelo professor(a).'),\n"
                + "	('Abdução Total de Ombros', 'Musculação', 'Ombro', 'Exercício para fortalecimento e hipertrofia dos músculos da região dos ombros.', '1. Em pé, afaste os pés na largura do quadril; 2. Mantenha uma postura ereta e olhe para frente; 3. Faça um leve contração abdominal e mantenha braços posicionados ao lado do corpo com a palma das mãos voltadas para a frente; 4. Deixe os cotovelos relaxados; 5. Eleve os braços lateralmente, sem flexioná-los, até as mãos ficarem estendidas acima da cabeça; 6. Retorne a posição inicial de forma controlada e repita os movimentos.'),\n"
                + "	('Push Press', 'Crossfit', 'Ombro', 'É um exercício que ajuda a fortalecer o tronco, ao mesmo tempo que ajuda a detectar algumas deficiências na mobilidade dos atletas. É também um bom exercício para treinar cargas pesadas acima da cabeça, em um ponto de vista de força pura.', '1. Na posição inicial, o peso deve estar nos calcanhares, vamos descer a anca e o joelho vai avançar; 2. Os ombros, a anca e os tornozelos devem estar todos alinhados no mesmo plano, como se tivéssemos uma parede atrás de nós; 3. Durante a execução, a descida vai ser curta e rápida de modo a empurrarmos a barra lá para cima.'),\n"
                + "	('Alongamento Torácico', 'Mobilidade', 'Peito', 'Exercício de alongamento para região torácica, preparando para a atividade física.', '1. Fique em quatro apoios, mantendo as mãos na distância dos ombros e os joelhos na distância dos quadris; 2. Deslize lentamente as mãos para a frente, abaixando o peito em direção ao chão; 3. Mantenha seus braços estendidos sem encostar no chão; 4. Permaneça nessa posição pelo tempo prescrito pelo professor e depois retorne lentamente à posição inicial.'),\n"
                + "	('Supino Inclinado com Barra', 'Musculação', 'Peito', 'Exercício para fortalecimento e hipertrofia da região peitoral, com enfoque nos músculos peitoral maior e menor, músculos auxiliares deltoides. Realiza com barra em uma prancha inclinada. Utilizado para moldar a parte superior dos peitorais. Indicado a praticante de musculação nível iniciante e intermediário.', '1. Posicione as pernas no suporte do banco inclinado o e deite sobre ele; 2. As costas bem estabilizadas no banco, pegue a barra a uma distância maior do que a dos ombros; 3. Levante o peso do suporte na linha do peitoral com os cotovelos estendidos; 4. Manter os punhos retos, desça a barra de maneira controlada e, ao mesmo tempo, realize a flexão dos cotovelos para baixo; 5. Empurre a barra para cima concentrando a força nos músculos do peito; 6. Repita os movimentos, conforme o número de repetições orientado pelo professor.'),\n"
                + "	('Ativação de Serrátil Anterior', 'Pilates', 'Peito', 'O exercício tem como objetivo trabalhar a ativação do músculos Serrátil Anterior e mobilização do ombro, auxiliando na realização de outras atividades físicas e prevenindo eventuais lesões.', '1.  Passe a faixa elástica por trás, nas costas; 2. Segure as pontas do elástico um em cada mão, então mantenha os braços esticando o elástico na altura do peito; 3. Leve os ombros para frente o máximo que conseguir, retorne levando o ombro o máximo que conseguir para trás; 4. Retorne a posição inicial e repita os movimentos.'),\n"
                + "	('Alongamento do Peitoral', 'Alongamento', 'Peito', 'Exercício de alongamento para a peito, preparando para a atividade física.', '1. De pé e de costas para o espaldar; 2. Deixe os pés próximos e as mãos apoiadas segurando o espaldar; 3. Inclinar o tronco para frente permanecendo com a coluna em alinhamento; 4. Segurar esta posição pelo tempo previamente estipulado pelo professor.'),\n"
                + "	('Afundo Alternado', 'Musculação', 'Perna', 'O exercício irá trabalhar o fortalecimento e hipertrofia dos músculos do glúteo e quadríceps.', '1. Pés afastados na largura dos quadris; 2. Mantenha o queixo paralelo ao chão e um arco natural na lombar; 3. Contraia o abdômen e com a perna direita de um passo largo para trás; 4. Desça os quadris até que o joelho da frente forme um ângulo de 90° graus. Pare embaixo e volte lentamente para a posição inicial; 5. Repita o mesmo movimento, agora dando um passo para trás com a perna esquerda; 6. Realize as repetições indicada pelo professor(a) alternando entre a perna direita e esquerda.'),\n"
                + "	('Agachamento com Elevação do Calcanhar', 'Funcional', 'Perna', 'O exercício trabalha a hipertrofia das pernas, como também auxilia na mobilização e fortalecimento do quadril, que auxilia na prática de atividades físicas e atividades diárias.', '1. Em pé, coloque os calcanhares sobre um apoio; 2. Realizar o agachamento sobre o apoio com os braços para frente, ou apoiados na cintura; 3. Retorne a posição inicial e repita o movimento pela quantidade de repetições determinadas pelo professor(a).'),\n"
                + "	('Agachamento com Salto', 'Funcional', 'Perna', 'Uma variação de agachamento que trabalha, além do fortalecimento de pernas, aumenta a força explosiva, potência muscular, a força de tendões e de ligamentos. Possuí uma dificuldade de nível intermediário.', '1. Posicione-se em pé, tronco reto e pés na largura dos ombros; 2. Realize o agachamento, mantendo abdômen contraído e força nas pernas, descendo até os joelhos ultrapassarem ligeiramente a ponta dos pés, com o quadril flexionando para trás durante o movimento, inclinando ligeiramente o tronco; 3. Durante o movimento de subida realize um salto, impulsionando o máximo que conseguir; 4. Na fase descendente do salto, procure cair na mesma posição referencial de saída, mas já em direção a um novo movimento de agachamento.'),\n"
                + "	('Flexão de Quadril Deitado', 'Alongamento', 'Perna', 'O exercício irá trabalhar o alongamento da cadeia posterior, auxiliando na execução de atividades diárias e atividades físicas.', '1. Deitado de barriga para cima e com as pernas estendidas; 2. Eleve uma das pernas, realizando uma flexão do quadril; 3. Retorne a posição inicial e ao finalizar as repetições, realize os movimento com a outra perna.'),\n"
                + "	('Flexão Nórdica', 'Musculação', 'Perna', 'É um ótimo exercício para fortalecer os músculos posteriores da coxa, além de trabalhar a hipertrofia dos mesmos.', '1. De costas ao aparelho, coloque os pés abaixo das almofadas e apoie os joelhos na base do aparelho; 2. Ajoelhado sobre o aparelho, inicie o movimento inclinando lentamente o tronco para frente e apoie os braços quando chegar próximo ao chão; 3. Em seguida, retorne a posição inicial concentrando sua força nos músculos posteriores da coxa; 4. Repita os movimentos pela quantidade de vezes indicado pelo professor(a).'),\n"
                + "	('Leg Press Inclinado', 'Musculação', 'Perna', 'Exercício para fortalecimento e hipertrofia dos músculos da coxa, com enfoque aos músculos vasto laterais, mediais, reto femorais e estimulo dos músculos auxiliares, tais como: os glúteos, tibial anterior e bíceps femoral.', '1. Posicione no aparelho, costas apoiadas no encosto; 2. Pés em uma distância similar a largura dos ombros; 3. Com os joelhos semiflexionados, posicione os pés um pouco à frente do corpo na plataforma; 4. Manter os pés e joelhos alinhados durante a execução; 5. Coluna reta e abdômen contraído, segure os pegadores para auxílio na estabilidade; 6. Destrave o aparelho e empurre a plataforma estendendo os joelhos; 7. Retorne à posição inicial flexionando os joelhos e repita os movimentos.'),\n"
                + "	('Mãos nos Pés', 'Alongamento', 'Perna', 'Exercício para alongamento dos membros inferiores, tais como: o bíceps femoral e tíbia posterior.', '1. Sente com as pernas estendidas sobre um colchonete; 2. Manter a coluna reta; 3. Incline o tronco para frente e com as mãos na tentativa de tocar as pontas dos pés; 4. Manter nessa posição entre 20 a 30 segundos.'),\n"
                + "	('Tríceps Banco', 'Musculação', 'Tríceps', 'Exercício para fortalecimento e hipertrofia dos músculos tríceps, com enfoque o tríceps braquial.', '1. Sente sobre um banco e apoie as mãos próximas do corpo; 2. Apoiar os pés no solo à frente com os joelhos ligeiramente flexionados; 3. Suspenda o corpo para frente e os braços estendidos nas extremidades do banco; 4. Manter a coluna alinhada e abdômen contraído, essa é a posição inicial do exercício; 5. Desça o corpo de forma controlada, flexionando os cotovelos o máximo que conseguir; 6. Retorne à posição inicial estendendo os cotovelos; 7. Concentre todo esforço nos músculos do tríceps, repita os movimentos.'),\n"
                + "	('Tríceps Francês', 'Musculação', 'Tríceps', 'Exercício para fortalecimento e hipertrofia dos músculos tríceps, com enfoque o tríceps braquial.', '1. Sentado em um banco, segure um haltere com as duas mãos; 2. Posicione o peso atrás da cabeça com os cotovelos flexionados; 3. Braços próximos da cabeça com as palmas das mãos voltadas para cima; 4. Empurre o peso para cima subindo os antebraços com a força dos tríceps, deixando os cotovelos quase estendidos; 5. Retorne à posição inicial de forma controlada movimentando somente os antebraços; 6. Durante a execução, o restante do corpo deverá ficar imóvel; 7. Repita os movimentos, conforme o número de repetições orientado pelo professor.'),\n"
                + "	('Rosca Alternado e Direto', 'Musculação', 'Bíceps', 'O exercício tem como objetivo trabalhar com maior intensidade o fortalecimento e hipertrofia dos músculos do bíceps.', '1. Utilize dois dumbbells ou halteres; 2. Posição em pé, cabeça e costas alinhadas, joelhos semiflexionados, pés ligeiramente afastados; 3. Braços estendidos e, com os antebraços posicionados em supinação; 4. Realize o movimento de Rosca Alternada, que consistem em fazer a flexão unilateral do cotovelo levando o punho em direção ao ombro. Retorne à posição inicial de forma controlada e realize novamente com o membro contralateral; 5. Novamente retorne à posição inicial e realize o movimento de Rosca Direta, que consiste em fazer a flexão simultânea dos cotovelos levando os punhos em direção aos ombros; 6. Retorne a posição inicial e repita os movimentos alternando entre o movimento de Rosca Alternada e a Direta.'),\n"
                + "	('Rosca 21 Inversa', 'Musculação', 'Bíceps', 'Esse exercício tem como objetivo sobrecarregar o bíceps através de uma mistura de repetições parciais e completas, para aumentar significativamente o tempo sob tensão, influenciando e muito na hipertrofia muscular. Indicado a praticante de musculação nível avançado.', '1. Faça inicialmente, o movimento da posição inicial, com os cotovelos em extensão, até o ângulo de 90° (metade do movimento). Volte à posição inicial e repita o movimento; 2. Sem descanso, na ultima repetição, segure em 90 graus. Agora, faça as repetições da metade do movimento para o final; 3. Após isso, faça repetições completas.'),\n"
                + "	('Rosca Zottman', 'Musculação', 'Bíceps', 'Exercício para fortalecimento e hipertrofia da região do bíceps. Estimula também, os músculos do antebraço.', '1. Segure um par de halteres e posicione-se em pé com o tronco firme e reto; 2. Mantenha os braços estendidos com as palmas das mãos voltadas para frente; 3. Sem mover a parte superior dos braços, flexione os cotovelos e aproxime os halteres de seus ombros o máximo que puder; 4. As palmas das mãos devem ficar voltadas para o corpo no topo da flexão; 5. No topo do movimento, gire seus pulsos de forma que as palmas da mãos fiquem votadas para fora; 6. Abaixe os halteres de volta às laterais do corpo.'),\n"
                + "	('Rosca Scott', 'Musculação', 'Bíceps', 'Um dos exercícios mais clássicos da musculação, consegue recrutar isoladamente as fibras que se propõe. Basicamente, por ser um exercício mono articular, a rosca scott imprime maior intensidade nos músculos dos bíceps.', '1. Sente no banco Scott; 2. Braços estendidos e repousados sobre o apoio, realize a pegada supinada na barra, as mãos devem estar alinhadas com os ombros; 3. Com o dorso das mãos voltado para baixo, flexione os cotovelos movendo a barra em direção aos ombros realizando a concentração máxima dos músculos; 4. Baixe o peso até a posição inicial até o ponto em que os braços fiquem semi-estendidos; 5. Flexione novamente, repetindo os movimentos.'),\n"
                + "	('Glúteo em Pé no Aparelho', 'Musculação', 'Glúteo', 'Exercício para hipertrofia e fortalecimento dos músculos do glúteo.', '1. Coloque-se de pé virado para os suportes acolchoados; 2. Apoie os cotovelos no suporte acolchoado e agarre as pegas com as mãos; 3. Coloque uma das pernas contra a plataforma móvel da máquina; 4. Empurre a plataforma estendendo o mais possível a perna; 5. Deixe a perna regressar à posição original baixando-a e dobrando os joelhos.'),\n"
                + "	('Glúteo no Cross Over', 'Musculação', 'Glúteo', 'O movimento executado neste exercício, proporciona estímulo para o treinamento dos músculos do glúteo, principalmente se o puxador for posicionado na altura dos tornozelos.', '1. Posicione próximo ao aparelho Cross Over; 2. Em uma das pernas, coloque o puxador fivela na altura do tornozelo; 3. Incline o troco levemente para frente, posicione as mãos no aparelho para lhe proporcionar mais estabilidade na execução; 4. Manter o troco estável, utilize a perna onde está posicionado o puxador eleve-a o mais alto que conseguir, mentalizando todo esforço sobre o músculo trabalhado; 5. Retorne à posição inicial e repita os movimentos.'),\n"
                + "	('Crucifixo Declinado com Halteres', 'Musculação', 'Peito', 'Exercício para fortalecimento e hipertrofia da região peitoral e deltoides. Possuí uma dificuldade de nível iniciante.', '1. Segure um par de halteres e deite-se de costas em um banco declinado; 2. Levante os halteres acima do peito, com as palmas das mãos voltadas uma para a outra; 3. Flexione os cotovelos ligeiramente; 4. Abaixe os halteres devagar para os lados e para longe do corpo formando um arco com os braços; 5. Quando os halteres estiverem praticamente alinhados com o peito, inverta o movimento e retorne à posição inicial; 6. Contraia os músculos peitorais no topo do movimento.'),\n"
                + "	('Supino Inclinado com Halteres', 'Musculação', 'Peito', 'Exercício para fortalecimento e hipertrofia da região peitoral, com enfoque nos músculos peitoral maior e menor, músculos auxiliares deltoides anteriores. Realiza em um banco inclinado. Ajuda a modelar e tonificar a parte superior do corpo, estimula a coordenação motora.', '1. Pegue dois halteres e deite sobre um banco inclinado com as constas e os glúteos estabilizado no banco; 2. Segure os halteres próximos um do outro acima do peito com os cotovelos estendidos; 3. Flexione os cotovelos, desça os pesos ao lado do corpo até a altura do peito; 4. Com a força dos músculos peitorais, empurre os pesos para cima retornando à posição inicial; 5. Repita os movimentos, conforme o número de repetições orientado pelo professor.'),\n"
                + "	('Supino Reto com Barra', 'Musculação', 'Peito', 'Um dos exercícios mais populares para o trabalho e desenvolvimento de força e hipertrofia da região dos músculos peitorais, com enfoque aos músculos peitoral maior e menor.', '1. Posicione os pés no suporte do banco e deite sobre ele; 2. As costas bem estabilizadas no banco, pegue a barra a uma distância maior do que a dos ombros; 3. Levante o peso do suporte na linha do peitoral com os cotovelos estendidos; 4. Manter os punhos retos, desça a barra de maneira controlada e, ao mesmo tempo, realize a flexão dos cotovelos para baixo; 5. Empurre a barra para cima concentrando a força nos músculos do peito; 6. Repita os movimentos.'),\n"
                + "	('Flexão de Braços na Barra Paralela', 'Musculação', 'Tríceps', 'Exercício para fortalecimento e hipertrofia dos músculos tríceps, com enfoque aos tríceps braquial.', '1. Segure nas barras com as palmas das mãos voltadas para dentro e com os polegares apontados para frente; 2. Suspenda o corpo sobre a barra com os cotovelos estendidos e o tronco ligeiramente inclinado para frente; 3. Manter a coluna reta durante a execução dos exercícios, essa é a posição inicial; 4. Desça o corpo lentamente flexionando os cotovelos; 5. No ponto mais baixo, retorne à posição inicial empurrando o corpo para cima; 6. Concentre a força nos músculos dos tríceps; 7. Repita os movimentos, conforme o número de repetições orientado pelo professor.'),\n"
                + "	('Panturrilha Sentado', 'Musculação', 'Perna', 'Exercício para fortalecimento e hipertrofia dos músculos das panturrilhas.', '1. Sente sobre o banco do aparelho; 2. Ponta dos pés na plataforma onde os calcanhares deverão ficar para fora da plataforma; 3. Joelhos abaixo das almofadas do suporte; 4. Manter a coluna ereta e abdome contraído; 5. Utilize os músculos da panturrilha para subir os calcanhares o mais alto que conseguir; 6. Destrave o aparelho, retorne à posição inicial de forma controlada; 7. Suba novamente os calcanhares e repita os movimentos..'),\n"
                + "	('Panturrilha Vertical', 'Musculação', 'Perna', 'Exercício para fortalecimento e hipertrofia dos músculos das panturrilhas, possuindo diversas variações. Realiza com o auxílio de um step e algumas anilhas posicionadas ao solo.', '1. Ajuste o aparelho na altura desejada, na posição completamente ereta; 2. Posicione os ombros abaixo das almofadas do suporte; 3. Posicione os pés ligeiramente separados; 4. Manter a coluna reta e abdome contraído; 5. Empurre o seu corpo para cima elevando os calcanhares do chão; 6. Concentre a força nos músculos da panturrilha; 7. Eleve os calcanhares o mais alto que conseguir; 8. Retorne à posição inicial de forma controlada e repita os movimentos.'),\n"
                + "	('Remada Baixa Neutra', 'Musculação', 'Costas', 'É um dos melhores exercícios para trabalhar costas, envolvendo diversos músculos, mas principalmente o latíssimo do dorso e o trapézio, nas porções inferior e superior.É um dos melhores exercícios para trabalhar costas, envolvendo diversos músculos, mas principalmente o latíssimo do dorso e o trapézio, nas porções inferior e superior.', '1. Sente no banco e mantenha a coluna ereta; 2. Com os braços estendidos segure o triângulo; 3. Puxe a triângulo em direção ao tórax, projetando os cotovelos para trás na maior altura possível, comprimindo, simultaneamente, as escápulas; 4. Estenda os braços até voltar para a posição inicial.'),\n"
                + "	('Remada Baixa Supinada', 'Musculação', 'Costas', 'É um dos melhores exercícios para trabalhar costas, envolvendo diversos músculos, mas principalmente o latíssimo do dorso e o trapézio, nas porções inferior e superior.', '1. Sente no banco e mantenha a coluna ereta; 2. Com os braços estendidos segure a barra com uma pegada supinada; 3. Puxe a barra na direção do abdômen, projetando os cotovelos para trás na maior altura possível, comprimindo, simultaneamente, as escápulas; 4. Estenda os braços até voltar para a posição inicial.'),\n"
                + "	('Remada Curvada com Barra', 'Musculação', 'Costas', 'Exercício efetivo que abrange costas ou dorsais. Realiza um movimento composto, assim como, o agachamento ou levantamento terra que estimula grandes músculos e demanda consciência corporal. Indicado a praticante de musculação nível avançado.', '1. Na posição em pé, pernas afastadas, joelhos levemente flexionados; 2. Segure uma barra com as mãos na mesma largura dos ombros, pegada em pronação; 3. Com a coluna alinhada, incline o troco para frente e mova o quadril para trás, até a barra na altura das coxas com os cotovelos estendidos; 4. Puxe a barra até a cintura, flexionando os cotovelos na lateral do corpo; 5. Segure a contração por um instante e retorne a barra à posição inicial de forma controlada; 6. Repita o movimento.'),\n"
                + "	('Abdominal Infra', 'Funcional', 'Abdômen', 'Exercício para fortalecimento e hipertrofia da região abdominal, reto abdominal, com enfoque na parte infra. Indicado a praticante de musculação nível iniciante e intermediário. Fácil execução.', '1. Deite sobre um colchonete; 2. Mãos entrelaçadas atrás do pescoço, joelhos flexionados; 3. Eleve as pernas mantendo os joelhos flexionados o mais alto que possa suportar; 4. Durante a execução do movimento, lembre-se, mantenha os joelhos flexionados, concentrando a força nos músculos abdominais; 5. Retorne à posição inicial e repita os movimentos.'),\n"
                + "	('Abdominal Oblíquo', 'Funcional', 'Abdômen', 'Exercício para fortalecimento e hipertrofia da região abdominal, reto abdominal, oblíquos, com enfoque, no oblíquo lateral. Indicado a praticante de musculação nível intermediário e avançado. Auxilia a moldar a região abdominal.', '1. Deite sobre um colchonete; 2. Joelhos flexionados, pés ligeiramente separados para maior estabilidade; 3. Mãos entrelaçadas atrás do pescoço, cotovelos abertos; 4. Realize o movimento de rotação do corpo, elevando o cotovelo em direção a outra perna, concentrando a força na parte lateral dos músculos abdominais; 5. Retorne à posição inicial, realize o movimento de rotação novamente, inverso ao anterior.'),\n"
                + "	('Supino Vertical na Máquina', 'Musculação', 'Peito', 'Exercício para fortalecimento e hipertrofia da região peitoral, com enfoque aos músculos peitoral maior e menor. Realize no aparelho com auxílio de roldanas. Indicado a praticante de musculação nível iniciante e intermediário.', '1. Deite sobre o aparelho, costas apoiadas; 2. Agarre os pegadores do aparelho com os cotovelos flexionados; 3. Com a força dos peitorais, empurre para frente até que os braços estejam estendidos; 4. Concentre toda a força nos músculos peitorais, retorne à posição inicial de forma controlada flexionando os cotovelos; 5. Repita os movimentos, conforme o número de repetição orientado pelo professor.'),\n"
                + "	('Crucifixo no Voador', 'Musculação', 'Peito', 'Exercício para fortalecimento e hipertrofia dos músculos peitorais, com enfoque aos músculos peitoral maior e menor e músculos auxiliares, tais como: deltoides anteriores.', '1. Sente no aparelho, costas imobilizadas nos banco e ombros encaixados para trás; 2. Pés sobre o suporte ou firmes no solo; 3. Agarre os pegadores com as palmas das mãos voltadas para frente; 4. Cotovelos ligeiramente flexionados e braços paralelos em relação ao solo; 5. A altura do banco deverá estar ajustada para a altura em que os pegadores fiquem na linha do peito; 6. Empurre os pegadores para o centro, com a força dos músculos peitorais; 7. Retorne à posição inicial e repita os movimentos.'),\n"
                + "	('Tríceps Corda no Cross Over', 'Musculação', 'Tríceps', 'Exercício para fortalecimento e hipertrofia dos músculos tríceps, com enfoque o tríceps braquial.', '1. Conecte uma corda no cabo do aparelho; 2. Pegue nas extremidades da corda com as palmas das mãos voltadas para dentro; 3. Cotovelos flexionados e fixos ao lado do corpo; 4. Manter a coluna reta e o tronco levemente inclinado para frente, essa é a posição inicial do exercício; 5. Entender os cotovelos até a posição em que os braços ficam totalmente estendidos; 6. Ao mesmo tempo, realize a abertura da pegada distanciando as mãos; 7. Concentre todo o esforço no músculo do tríceps; 8. Retorne à posição inicial de forma controlada e repita os movimentos.'),\n"
                + "	('Tríceps Coice na Polia', 'Musculação', 'Tríceps', 'Esse exercício trabalha o fortalecimento e hipertrofia do tríceps braquial, sendo realizado na polia a tensão muscular se mantêm uniforme durante todo o movimento trazendo melhores resultados.', '1. Com a polia posicionada na parte mais baixa, iniciar o movimento curvando o tronco para a frente e mantendo os ombros estáveis e o braços paralelo ao solo; 2. Depois realizar o movimento de extensão até a contração máxima do tríceps e após isso retornar lentamente à posição inicial.'),\n"
                + "	('Puxador com Triângulo', 'Musculação', 'Costas', 'É um excelente exercício para o treino de costas, trazendo benefícios como a melhora de postura, correção de desiquilíbrio e definição muscular.', '1. Sente-se no aparelho, de modo que os joelhos fiquem apoiados sob o apoio frontal e os pés apoiados no chão; 2. Segure o triângulo de modo que os cotovelos fiquem levemente flexionados e mantenha os cotovelos paralelos; 3. Contraia o abdômen, mantenha a coluna reta; 4. Dobre os cotovelos e puxe o triângulo para baixo, em direção ao seu peito; 5. Desça até que o triângulo fique abaixo de seu queixo; 6. Estique os braços lentamente, voltando à posição inicial.'),\n"
                + "	('Remada Unilateral', 'Musculação', 'Costas', 'Exercício com enfoque para hipertrofia e fortalecimento dos músculos dorsais. Na execução, estimula também os músculos auxiliares, tais como: bíceps e trapézio.', '1. Utilize um banco, apoie o joelho e a mão direita sobre ele; 2. O pé esquerdo deve ficar afastado do corpo; 3. Com o tronco na posição horizontal, segure o peso com a mão esquerda; 4. Puxe o halter até a altura do quadril, flexionando o cotovelo para trás do corpo, concentrando a força nos músculos das costas; 5. Desça o peço novamente à posição inicial de forma controlada; 6. Ao término das repetições, repita os movimentos para o outro lado.'),\n"
                + "	('Levantamento Terra', 'Musculação', 'Corpo', 'O levantamento terra é ótimo exercício para aumentar força e potência muscular. Ele trabalha os principais músculos do corpo: eretor da espinha, glúteos, quadríceps, trapézio, latíssimo do dorso, deltoide posterior, antebraço e até bíceps femoral.  Indicado a praticante de musculação nível intermediário.', '1. Fique em pé, mantenha os pés afastados e alinhados com os ombros; 2. Flexione levemente os joelhos e segure uma barra com as duas mãos com uma pegada pronada; 3. Posicione as mãos um pouco mais afastadas que a largura dos ombros; 4. Mantenha o tórax e as costas retas e empurre os quadris para trás, enquanto abaixa a barra logo abaixo dos joelhos; 5. Faça uma pausa e, em seguida, reverta o movimento para retornar à posição inicial.'),\n"
                + "	('Rosca Direta', 'Musculação', 'Bíceps', 'Exercício para fortalecimento e hipertrofia dos bíceps, com enfoque aos músculos bíceps braquiais. Realiza de forma lenta e controlada.', '1. Na posição em pé, realize a pegada em supinação na barra reta em uma distância semelhante a dos ombros; 2. Cabeça e costas alinhadas, joelhos semiflexionados, pés ligeiramente separados para se obter uma base estável na execução; 3. Braços estendidos, realize a flexão dos cotovelos contraído os músculos dos bíceps, trazendo a barra em direção aos ombros, efetuando a contração máxima dos músculos; 4. Retorne à posição inicial de forma controlada e repita os movimentos.'),\n"
                + "	('Rosca Direta com Barra W', 'Musculação', 'Bíceps', 'A execução da rosca direta com uma barra W muda a pegada. Da pegada completamente supinada as mãos trocam para uma pegada menos supinada, praticamente neutra (palmas das mãos voltadas para dentro). Essa posição das mãos enfatiza a cabeça longa (externa) do bíceps e o braquial, sendo menos árdua para a articulação do punho.', '1. Em pé, mantenha os pés afastados na largura do quadril e os joelhos levemente flexionados, criando uma base que proporcione bastante estabilidade e equilíbrio; 2. Segure a barra W com os braços estendidos ao lado do tronco; 3. Deixe a palma das mãos voltadas para a frente; 4. Flexione os braços e leve os pesos até aproximadamente a altura dos ombros, fazendo um movimento concentrado e mantendo os cotovelos próximos ao tronco; 5. Desça devagar até quase estender os braços.'),\n"
                + "	('Abdominal Supra', 'Musculação', 'Abdômen', 'Exercício para fortalecimento e hipertrofia da região abdominal, reto abdominal, com enfoque na parte supra. Indicado a praticante de musculação nível iniciante e intermediário. Fácil execução.', '1. Deite sobre um colchonete; 2. Joelhos flexionados, pés ligeiramente separados para maior estabilidade; 3. Mãos entrelaçadas atrás do pescoço, manter o queixo alinhado ao peitoral evitando sobrecarregar o pescoço; 4. Realize a flexão do tronco elevando os deltoides; 5. Retorne à posição inicial e repita os movimentos.'),\n"
                + "	('Agachamento Livre', 'Musculação', 'Perna', 'Exercício para fortalecimento e hipertrofia dos músculos da coxa e glúteos, o agachamento possui diversas variações e uma delas é o Agachamento livre. Trabalha os músculos da coxa e auxiliares, tais como, os glúteos.', '1. Estenda os braços à frente em uma altura em que eles estejam em paralelo ao solo; 2. Posicione os pés separadamente em uma largura semelhante à dos ombros; 3. Com a coluna reta e abdômen contraído, desça o corpo flexionando os joelhos até que a coxa fique em um ângulo de 90 graus, em paralelo ao solo; 4. Os joelhos não deveram ultrapassar a linha dos pés; 5. Com a força das coxas, empurre o corpo para cima retornando à posição inicial; 6. Repita os movimentos.'),\n"
                + "	('Banco Extensor', 'Musculação', 'Perna', 'Exercício para fortalecimento e hipertrofia da região das coxas, com enfoque aos músculos reto femorais, vasto laterais e medias. Realiza no aparelho, indicado aos praticantes que desejam realizar um trabalho muscular isolado dos quadríceps.', '1. Regule o aparelho de acordo com a altura das pernas; 2. Sente na cadeira e coloque os pés logo abaixo do apoio; 3. O ajuste correto do aparelho, os joelhos ficam em um ângulo de 90 graus e acomodados logo acima, na borda do banco; 4. Manter as costas retas e apoiadas no encosto do banco, segure os pegadores; 5. Empurre o peso para cima, estendendo os joelhos até que as pernas fiquem na posição horizontal; 6. Retorne à posição inicial de forma controlada e repita os movimentos.'),\n"
                + "	('Leg Press', 'Musculação', 'Perna', 'Exercício para fortalecimento e hipertrofia dos músculos da coxa, com enfoque aos músculos vasto laterais, mediais, reto femorais e estimulo dos músculos auxiliares, tais como, os glúteos.', '1. Sente no aparelho com os joelhos flexionados; 2. Posicione os pés na plataforma ligeiramente separados; 3. Manter as costas restas e glúteos bem apoiados; 4. Utilize as mãos para agarrar os pegadores; 5. Com a força dos músculos da coxa, empurre a plataforma para frente, até que os joelhos fiquem em uma posição semiflexionada; 6. Retorne lentamente à posição inicial; 7. Repita os movimentos;'),\n"
                + "	('Banco Adutor', 'Musculação', 'Perna', 'Exercício para fortalecimento e hipertrofia dos músculos da coxa, com enfoque a região interna próximo a virilha. Trabalha os músculos adutor longo.', '1. Sente no banco do aparelho adutor, costas e glúteos bem apoiados; 2. Posicione os pés no suporte da máquina e joelhos apoiados no encosto interno lateral; 3. Concentre a força nos músculos trabalhados adutores; 4. Empurre o suporte lateral com os joelhos, trazendo-os de forma simultânea um próximo ao outro; 5. Manter a contração máxima dos músculos por um instante; 6. Retorne à posição inicial de forma controla, repita os movimentos.'),\n"
                + "	('Desenvolvimento Arnold', 'Musculação', 'Ombro', 'Exercício para fortalecimento e hipertrofia dos músculos da região dos ombros, trabalhando também, músculos auxiliáreis do trapézio. Estimula a coordenação motora.', '1. Sente em um banco com encosto para as costas e segure um haltere em cada mão; 2. Flexione os cotovelos, trazer eles até ficarem próximos ao peito com as palmas das mãos voltadas para o corpo; 3. Com os halteres na altura do pescoço e, com ajuda dos músculos dos ombros, empurre os pesos para cima e,  simultaneamente, gire os punhos para o lado de dentro; 4. Ao topo do movimento, os braços deverão ficar estendidos; 5. Retorne à posição inicial de forma controlada e repita os movimentos.'),\n"
                + "	('Elevação Lateral', 'Musculação', 'Ombro', 'Exercício para fortalecimento e hipertrofia dos músculos da região dos ombros, com enfoque na lateral dos deltoides.', '1. Na posição em pé, segure dois halteres com as palmas das mãos voltadas a lateral do corpo; 2. Pés afastados, aproxime os pesos um ao outro na altura da coxa, cotovelos ligeiramente flexionados; 3. Manter a coluna reta, eleve os halteres lateralmente até altura em que os braços fiquem paralelos ao solo; 4. Concentre o esforço na parte lateral dos deltoides e deixe os cotovelos imóveis; 5. Retorne à posição inicial de forma controlada, evite pendular o tronco na execução; 6. Repita os movimentos, conforme o número de repetições orientado pelo professor.'),\n"
                + "	('Elevação Frontal Alternada', 'Musculação', 'Ombro', 'Exercício para fortalecimento e hipertrofia dos músculos da região dos ombros. Realiza em pé, exige que o praticante possua músculos auxiliarem fortalecidos, tais como: admonais e lombares.', '1. Segure dois alteres com as palmas das mãos voltadas para o corpo; 2. Posicione os pesos à frente, cotovelo levemente flexionados; 3. Manter o troco reto, escápulas encaixadas para trás e abdômen contraído; 4. Cotovelos semiflexionados, levantar um halter com a força do ombro até a altura em que o braço fique em paralelo ao solo; 5. Retorne à posição inicial e levantar o outro braço; 6. Repita os movimentos pelo número de repetições orientado pelo professor.'),\n"
                + "	('Encolhimento com Barra', 'Musculação', 'Ombro', 'Esse exercício ajuda a trabalhar a contração de toda a região do trapézio. Dessa maneira, é possível desenvolver os músculos e estimular tanto o crescimento como o fortalecimento.', '1. Fique de pé com a largura dos ombros e segure uma barra com pegada pronada; 2. Relaxe os ombros para que eles pendurem o mais baixo possível; 3. Levante a barra lentamente levantando os ombros em um movimento único e vertical; 4. Aguarde uma contagem de um; 5. Relaxe lentamente os ombros para voltar à posição de início.'),\n"
                + "	('Abdominal Infra com Elevação de Pernas', 'Funcional', 'Abdômen', 'Exercício para fortalecimento e hipertrofia da região abdominal, reto abdominal, com enfoque na parte infra. Trabalha também os músculos auxiliares, tais como: os flexores do quadril. Indicado a praticante de musculação nível intermediário e avançado.', '1. Deite sobre um colchonete; 2. Como posição inicial do exercício, tronco paralelo ao solo, pernas estendidas e elevadas na linha do quadril; 3. Braços estendidos paralelo ao troco, estenda-os para criar uma base na execução do exercício; 4. Concentre o esforço nos músculos abdominais, eleve o quadril realizando a flexão parcial do tronco o mais alto que possa suportar; 5. Retorne à posição inicial e repita os movimentos.');";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public int checkExistingExercises() {
        String sql = "SELECT COUNT(*) AS `amount` FROM `exercise`;";
        int exercisesAmount = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                exercisesAmount = rs.getInt("amount");
                return exercisesAmount;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return exercisesAmount;
    }
}
